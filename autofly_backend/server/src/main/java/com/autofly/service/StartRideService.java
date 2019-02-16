package com.autofly.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.StartRideRequest;
import com.autofly.model.StartRideResponse;
import com.autofly.repository.dao.AutoDriverRepository;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.dao.PassengerRepository;
import com.autofly.repository.dao.RideRepository;
import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.Hotspot;
import com.autofly.repository.model.Passenger;
import com.autofly.repository.model.Ride;

@Component
public class StartRideService {

	@Autowired
	private AutoDriverRepository driverRepo;
	
	@Autowired
	private RideRepository rideRepo;
	
	@Autowired
	private PassengerRepository passengerRepo;
	
	@Autowired
	private HotspotAutoQueue hotspotAutoQueue;
	
	@Autowired
	private HotspotRepository hotspotRepo;
	
	private static final Date CURR_DATE = Date.valueOf(LocalDate.now());
	private static final String RIDE_WAITING = "Waiting";
	private static final String RIDE_STARTED = "Started";
	private static final String RIDE_COMPLETED = "Completed";
	
	private static final String PASSENGER_REQUESTED = "Requested";
	private static final String PASSENGER_BOARDED = "Boarded";
	private static final String PASSENGER_PAYMENT_PENDING = "Payment Pending";


	public StartRideResponse startRide(StartRideRequest request) {
		
		StartRideResponse response = new StartRideResponse();
		
		AutoDriver driver = driverRepo.findByUserId(request.getDriverId());;
		
		if(null == driver) {
			response.setSuccess(false);
			return response;
		}
		
		List<Ride> rideRequests = rideRepo.findByAutoIdAndDutyDateAndFromHotspotAndRideStatusAndPassengerStatusAndZoneId
				(driver.getUserId(), CURR_DATE, request.getFromHotspotId(), RIDE_WAITING, PASSENGER_BOARDED, request.getZoneId());
		
		if(rideRequests.isEmpty()) {
			response.setSuccess(false);
			response.setRideStatus(RIDE_WAITING);
			return response;
		}
		
		if(rideRequests.size() < 3) {
			response.setSuccess(false);
			response.setRideStatus(RIDE_WAITING);
			return response;
		}
		
		
		//First update each rideStatus
		rideRequests.stream()
					.forEach(r -> r.setRideStatus(RIDE_STARTED));
		
		rideRequests.stream()
					.forEach(r -> rideRepo.save(r));
		
		//Create Passenger List
		List<Passenger> passengerList = rideRequests.stream()
													.map(r -> r.getPassengerId())
													.map(p -> passengerRepo.findByUserId(p))
													.collect(Collectors.toList());
		
		if(null == passengerList || passengerList.isEmpty()) {
			response.setSuccess(false);
			response.setRideStatus(RIDE_WAITING);
			return response;
		}
		
		response.setPassengers(passengerList);
		
		//Remove auto from queue
		Hotspot currentHotspot = hotspotRepo.findById(request.getFromHotspotId()).orElse(null);
		if(null == hotspotAutoQueue.removeAutoFromHotspot(currentHotspot)) {
			response.setSuccess(false);
			response.setRideStatus("Internal Error");
			return response;
		}
		
		//Add rides to response
		response.setRides(rideRequests);
		response.setRideStatus(RIDE_STARTED);
		response.setSuccess(true);
		
		return response;
	
	}

	
}
