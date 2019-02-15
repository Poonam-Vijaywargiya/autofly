package com.autofly.model;


import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.Hotspot;
import com.autofly.repository.model.PassengerTrip;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@NoArgsConstructor
public class ConfirmTripResponse {

    private boolean success;
    private int passengerId;

    private int passengerTripId;
    private LatLng sourceLocation;
    private LatLng destinationLocation;
    private List<Hotspot> route;
    private double fare;
    private String tripStatus;



}
