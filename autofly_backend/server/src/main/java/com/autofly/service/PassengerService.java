package com.autofly.service;


import com.autofly.model.*;
import com.autofly.repository.dao.PassengerRepository;
import com.autofly.repository.dao.PassengerTripRepository;
import com.autofly.repository.dao.RideRepository;
import com.autofly.repository.model.Hotspot;
import com.autofly.repository.model.Passenger;
import com.autofly.repository.model.PassengerTrip;
import com.autofly.repository.model.Ride;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static final String TRIP_CONFIRMED = "CONFIRMED";
    private static final String TRIP_ONGOING = "ONGOING";
    private static final String TRIP_COMPLETED = "COMPLETED";
    private static final String TRIP_INITIATED= "INITIATED";


    private static final String PASSENGER_REQUESTED = "Requested";
    private static final String PASSENGER_BOARDED = "Boarded";




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

        if(ride != null) {
            ride.setRideStatus(PASSENGER_BOARDED);
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
}
