package com.autofly.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.FindAutoRequest;
import com.autofly.model.FindAutoResponse;
import com.autofly.repository.dao.AutoDriverRepository;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.dao.RideRepository;
import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.Hotspot;
import com.autofly.repository.model.Ride;

@Component
public class FindAutoService {

	@Autowired
	private HotspotAutoQueue hotspotAutoQueue;
	
	@Autowired
	private RideRepository rideRepo;
	
	@Autowired
	private HotspotRepository hotspotRepo;
	
	@Autowired
	private AutoDriverRepository driverRepo;
	
	private static final Date CURR_DATE = Date.valueOf(LocalDate.now());
	private static final String RIDE_WAITING = "Waiting";
	private static final String RIDE_STARTED = "Started";
	private static final String RIDE_COMPLETED = "Completed";
	
	private static final String PASSENGER_REQUESTED = "Requested";
	private static final String PASSENGER_BOARDED = "Boarded";
	
	public FindAutoResponse findAuto(FindAutoRequest request) {
		
		FindAutoResponse response = new FindAutoResponse();
		
		Hotspot currentHotspot = hotspotRepo.findById(request.getFromHotspotId()).orElse(null);
		if(null == currentHotspot) {
			response.setFoundAuto(false);
			return response;
		}
		currentHotspot.setCurrentZoneId(request.getZoneId());
		
		Hotspot destHotspot = hotspotRepo.findById(request.getToHotspotId()).orElse(null);
		if(null == destHotspot) {
			response.setFoundAuto(false);
			return response;
		}
		
		List<Ride> rideRequests = new ArrayList<>();
		AutoDriver driver = new AutoDriver();
		
		do {
			
			driver = hotspotAutoQueue.getFirstAutoInQueue(currentHotspot);
			
			if(null == driver) {
				response.setFoundAuto(false);
				return response;
			}
			
//			driver = driverRepo.findByUserId(driver.getUserId());
			//Check if this auto has got max requests
			rideRequests = rideRepo.findByAutoIdAndDutyDateAndFromHotspotAndRideStatusAndPassengerStatusAndZoneId
					(driver.getUserId(), CURR_DATE, request.getFromHotspotId(), RIDE_WAITING, PASSENGER_REQUESTED, 
							request.getZoneId());
			
			
			if(rideRequests.size() >= 3) {
				 hotspotAutoQueue.removeAutoFromHotspot(currentHotspot);
				 
			} else {
				response.setFoundAuto(true);
				List<Integer> passengers = new ArrayList<>();
				
				passengers = rideRequests.stream()
										 .map(r -> r.getPassengerId())
										 .filter(p -> p!=0)
							             .collect(Collectors.toList());
				
				if(!passengers.isEmpty()) {
					driver.setBoardedPassengers(passengers);
					driver.setNoOfPassengersBoarded(passengers.size());
				}
			}
			
		} while(!response.isFoundAuto());

		
		//Create a Ride
		Ride ride = new Ride();
		ride.setAutoId(driver.getUserId());
		ride.setDutyDate(CURR_DATE);
		ride.setEarning(10.0);
		ride.setFromHotspot(request.getFromHotspotId());
		ride.setToHotspot(request.getToHotspotId());
		ride.setPassengerId(request.getPassengerId());
		ride.setPassengerStatus(PASSENGER_REQUESTED);;
		ride.setZoneId(request.getZoneId());
		ride.setRideStatus(RIDE_WAITING);
		ride.setPassengerTripId(request.getPassengerTripId());

		Ride savedRide = rideRepo.save(ride);
		
		if(null == savedRide) {
			response.setFoundAuto(false);
			return response;
		} 
		
		response.setRideId(savedRide.getRideId());
		response.setFoundAuto(true);
		response.setDriver(driver);
		
		return response;
	}

}
