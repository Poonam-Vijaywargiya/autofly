package com.autofly.model;


import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.PassengerTrip;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@NoArgsConstructor
public class ConfirmTripResponse {

    private boolean success;
    private PassengerTrip passengerTrip;
    private AutoDriver autoDriver;
    private LatLng boardingLocation;
}
