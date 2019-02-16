package com.autofly.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EndRideRequest {

    private int driverId;
    private int currentHotspotId;
    private int zoneId;

    private int passengerId;
    private int passengerTripId;
    private int rideId;
}