package com.autofly.service;


import com.autofly.model.*;
import com.autofly.repository.dao.*;
import com.autofly.repository.model.Hotspot;
import com.autofly.repository.model.Passenger;
import com.autofly.repository.model.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class EndRideService {

    @Autowired
    private AutoDriverRepository driverRepo;

    @Autowired
    private RideRepository rideRepo;

    @Autowired
    private PassengerRepository passengerRepo;

    @Autowired
    private PassengerTripRepository passengerTripRepo;

    @Autowired
    private HotspotRepository hotspotRepo;

    @Autowired
    private  FindHotspotZoneService findHotspotZoneService;


    private static final Date CURR_DATE = Date.valueOf(LocalDate.now());
    private static final String RIDE_WAITING = "Waiting";
    private static final String RIDE_STARTED = "Started";
    private static final String RIDE_COMPLETED = "Completed";

    private static final String PASSENGER_REQUESTED = "Requested";
    private static final String PASSENGER_BOARDED = "Boarded";
    private static final String PASSENGER_PAYMENT_PENDING = "Payment Pending";

    public EndRideResponse endRide(EndRideRequest request) {

        EndRideResponse response = new EndRideResponse();

        Ride ride = rideRepo.findById(request.getRideId()).orElse(null);

        if(ride != null) {
            ride.setRideStatus(RIDE_COMPLETED);
            ride.setPassengerStatus(PASSENGER_PAYMENT_PENDING);
            Ride updatedRide = rideRepo.save(ride);
            if(updatedRide != null) {
                response.setRideId(updatedRide.getRideId());
                response.setRideStatus(updatedRide.getRideStatus());
                response.setPassengerStatus(updatedRide.getPassengerStatus());
                response.setEarning(updatedRide.getEarning());
            }
        }
        else {
            return response;
        }

        AssignAutoRequest assignAutoRequest = new AssignAutoRequest();
        assignAutoRequest.setAssignedHotspot(request.getCurrentHotspotId());
        assignAutoRequest.setAssignedZone(ride.getZoneId());
        assignAutoRequest.setAvailable(true);
        assignAutoRequest.setDriverId(request.getDriverId());

        Boolean assignmentSuccess = findHotspotZoneService.assignAuto(assignAutoRequest);

        if(assignmentSuccess) {
            response.setDriverId(request.getDriverId());
            response.setCurrentHotspotId(request.getCurrentHotspotId());
            response.setZoneId(request.getZoneId());
            response.setSuccess(true);
        }

        return response;
    }
}
