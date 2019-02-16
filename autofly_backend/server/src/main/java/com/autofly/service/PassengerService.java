package com.autofly.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.AddPassengerRequest;
import com.autofly.model.AddPassengerResponse;
import com.autofly.model.ConfirmTripRequest;
import com.autofly.model.ConfirmTripResponse;
import com.autofly.model.EndTripRequest;
import com.autofly.model.EndTripResponse;
import com.autofly.model.FindAutoRequest;
import com.autofly.model.FindAutoResponse;
import com.autofly.model.LatLng;
import com.autofly.model.WalletRequest;
import com.autofly.model.WalletResponse;
import com.autofly.repository.dao.AutoDriverRepository;
import com.autofly.repository.dao.PassengerRepository;
import com.autofly.repository.dao.PassengerTripRepository;
import com.autofly.repository.dao.RideRepository;
import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.Hotspot;
import com.autofly.repository.model.Passenger;
import com.autofly.repository.model.PassengerTrip;
import com.autofly.repository.model.Ride;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class PassengerService {


    @Autowired
    PassengerRepository passengerRepo;

    @Autowired
    PassengerTripRepository passengerTripRepo;

    @Autowired
    FindAutoService findAutoService;

    @Autowired
    private RideRepository rideRepo;
    
    @Autowired
    private AutoDriverRepository driverRepo;

    private static final String TRIP_CONFIRMED = "CONFIRMED";
    private static final String TRIP_ONGOING = "ONGOING";
    private static final String TRIP_COMPLETED = "COMPLETED";
    private static final String TRIP_INITIATED= "INITIATED";


    private static final String PASSENGER_REQUESTED = "Requested";
    private static final String PASSENGER_BOARDED = "Boarded";
    private static final String PASSENGER_PENDING_MONEY = "Pending Money";
    private static final String PASSENGER_PAYMENT_SUCCESS = "Payment Success";
    
    private static final String RIDE_COMPLETED = "Completed";
    
    public WalletResponse checkWalletBalance(WalletRequest request) {

        WalletResponse response = new WalletResponse();

        Passenger passenger = passengerRepo.findByUserId(request.getPassengerId());

        boolean isCreditAvailable  = false;

        response.setPassengerId(request.getPassengerId());
        response.setFare(request.getFare());
            if(passenger != null && passenger.getWalletBalance() >= request.getFare()){
                isCreditAvailable = true;
                response.setCreditAvailable(isCreditAvailable);
            }
            else {
                response.setCreditAvailable(isCreditAvailable);
            }

        return response;
    }

    public ConfirmTripResponse confirmPassengerTrip(ConfirmTripRequest request){

        ConfirmTripResponse  response= new ConfirmTripResponse();

        int passengerId = request.getPassengerId();

        Gson gson = new Gson();

        Passenger passenger = passengerRepo.findByUserId(passengerId);

        if(passenger != null){
            String sourceLocation = gson.toJson(request.getSource());
            String destLocation = gson.toJson(request.getDestination());
            String route  = gson.toJson(request.getRoute());

            PassengerTrip passengerTrip  = new PassengerTrip();

            if(request.getPassengerTripId() != 0) {
                passengerTrip = passengerTripRepo.findById(request.getPassengerTripId()).orElse(new PassengerTrip());
            }
            passengerTrip.setPassengerId(passengerId);
            passengerTrip.setSource(sourceLocation);
            passengerTrip.setDestination(destLocation);
            passengerTrip.setRoute(route);
            passengerTrip.setFare(request.getFare());
            passengerTrip.setTripStatus(TRIP_INITIATED);

            PassengerTrip savedPassengerTrip = passengerTripRepo.save(passengerTrip);

            if(savedPassengerTrip != null) {
                response.setPassengerId(passengerTrip.getPassengerId());
                response.setPassengerTripId(passengerTrip.getId());
                response.setTripStatus(passengerTrip.getTripStatus());
                response.setSource(gson.fromJson(savedPassengerTrip.getSource(), LatLng.class));
                response.setDestination(gson.fromJson(savedPassengerTrip.getDestination(), LatLng.class));
                response.setRoute(gson.fromJson(savedPassengerTrip.getRoute(), new TypeToken<List<Hotspot>>(){}.getType()));
                response.setFare(savedPassengerTrip.getFare());
//                response.setSuccess(true);
            }


            FindAutoRequest findAutoRequest  = new FindAutoRequest();
            findAutoRequest.setPassengerId(passenger.getUserId());
            findAutoRequest.setFromHotspotId(request.getRoute().get(0).getId());
            findAutoRequest.setToHotspotId(request.getRoute().stream().filter(h -> {
                return h.getDropLocation() != null && h.getDropLocation() == true ? true : false;
            }).findFirst().orElse(new Hotspot()).getId());
            findAutoRequest.setZoneId(request.getRoute().get(0).getCurrentZoneId());
            findAutoRequest.setPassengerTripId(savedPassengerTrip.getId());

            /// Call find Auto App with above request,
            FindAutoResponse findAutoResponse = findAutoService.findAuto(findAutoRequest);


            // Forward auto details to final response
            if(findAutoResponse.isFoundAuto()){
                response.setDriver(findAutoResponse.getDriver());
                Ride ride = rideRepo.findById(findAutoResponse.getRideId()).orElse(null);
                if (ride != null) {
                    passengerTrip.setTripStatus(TRIP_CONFIRMED);
                    PassengerTrip updatedPassengerTrip = passengerTripRepo.save(passengerTrip);
                    response.setTripStatus(updatedPassengerTrip.getTripStatus());
                    response.setFoundAuto(findAutoResponse.isFoundAuto());
                    response.setRide(ride);
                    response.setSuccess(true);
                }
            }
            else {
                response.setSuccess(false);
            }
        }

        return response;
    }

    public AddPassengerResponse addPassenger(AddPassengerRequest request) {
        AddPassengerResponse response = new AddPassengerResponse();

        Ride ride  = rideRepo.findById(request.getRideId()).orElse(null);

        PassengerTrip passengerTrip = passengerTripRepo.findById(request.getPassengerTripId()).orElse(null);

        if( passengerTrip != null) {
            passengerTrip.setTripStatus(TRIP_ONGOING);
            PassengerTrip updatedPassengerTrip   = passengerTripRepo.save(passengerTrip);
            response.setTripStatus(updatedPassengerTrip.getTripStatus());
        }

        if(ride != null) {
            ride.setPassengerStatus(PASSENGER_BOARDED);
            Ride updatedRide   = rideRepo.save(ride);
            if(updatedRide != null ) {
                response.setPassengerId(request.getPassengerId());
                response.setPassengerTripId(request.getPassengerTripId());
                response.setRideId(updatedRide.getRideId());
                response.setSuccess(true);
            }
        }

        return  response;
    }

	public EndTripResponse endTrip(EndTripRequest request) {
		
		EndTripResponse response = new EndTripResponse();
		
		//Fetch Trip
		PassengerTrip trip = passengerTripRepo.findByIdAndTripStatus(request.getTripId(), TRIP_ONGOING);
		trip.setTripStatus(TRIP_COMPLETED);

		//Fetch Passenger Ride Details
		List<Ride> passengerRides = rideRepo.findByPassengerTripIdAndPassengerStatusAndRideStatus
				(trip.getId(), PASSENGER_PENDING_MONEY, RIDE_COMPLETED);
		
		if(null == passengerRides || passengerRides.isEmpty()) {
			response.setSuccess(false);
			return response;
		}
		
		//Fetch Passenger
		Passenger passenger = passengerRepo.findByUserId(trip.getPassengerId());
		
		//Get the auto drivers involved in the trip
		List<AutoDriver> drivers = passengerRides.stream()
												 .map(r -> r.getAutoId())
												 .map(a -> driverRepo.findByUserId(a))
												 .collect(Collectors.toList());
		List<Double> autoEarnings = passengerRides.stream()
												  .map(r -> r.getEarning())
												  .collect(Collectors.toList());
		//Payment Transaction
		Double passengerUpdatedWallet = this.paymentTransaction(passenger, trip.getFare(), drivers, autoEarnings);
		
		if(null == passengerUpdatedWallet) {
			response.setSuccess(false);
			return response;
		}
		
		//Update Trip and Ride		
		passengerTripRepo.save(trip);
		
		for(Ride r : passengerRides) {
			r.setPassengerStatus(PASSENGER_PAYMENT_SUCCESS);
			rideRepo.save(r);
		}
		
		response.setSuccess(true);
		response.setUpdatedWalletBallance(passengerUpdatedWallet);
		return response;
	}

	private Double paymentTransaction(Passenger passenger, double passengerFare, List<AutoDriver> drivers, List<Double> autoEarnings) {
		
		Double originalPassengerWallet = 0.0;
		List<Double> originalAutoWallet = new ArrayList<>();
		
		try {
			originalPassengerWallet = passenger.getWalletBalance();
			passenger.setWalletBalance(passenger.getWalletBalance() - passengerFare);
			passengerRepo.save(passenger);
			
			for(int i=0; i<drivers.size(); i++) {
				AutoDriver driver = drivers.get(i);
				driver.setWalletBalance(driver.getWalletBalance() + autoEarnings.get(i)) ;
				driverRepo.save(driver);
			}
			
			return passenger.getWalletBalance();
			
		} catch (Exception e) {
			// Revert the transaction
			passenger.setWalletBalance(originalPassengerWallet);
			passengerRepo.save(passenger);
			

			for(int i=0; i<drivers.size(); i++) {
				AutoDriver driver = drivers.get(i);
				driver.setWalletBalance(originalAutoWallet.get(i)) ;
				driverRepo.save(driver);
			}
			
			return null;
		}
	}
}
