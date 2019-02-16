package com.autofly.model;


import com.autofly.repository.model.Passenger;
import com.autofly.repository.model.Ride;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EndRideResponse {

    private boolean success;

    private int driverId;
    private int currentHotspotId;
    private int zoneId;

    private int passengerId;
    private int passengerTripId;
    private int rideId;
    private String rideStatus;
    private double earning;
    private String passengerStatus;
    
}