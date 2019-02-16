package com.autofly.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@NoArgsConstructor
public class AddPassengerResponse {
    private int passengerId;
    private int rideId;
    private int passengerTripId;
    private String tripStatus;
    private boolean success;
}
