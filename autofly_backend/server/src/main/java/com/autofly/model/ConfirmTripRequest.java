package com.autofly.model;

import com.autofly.repository.model.Hotspot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmTripRequest {

    private int passengerId;
    private LatLng source;
    private LatLng destination;
    private List<Hotspot> route;
    private double fare;
    private LocalDateTime departureTime;
    private LocalDateTime requestTimestamp = LocalDateTime.now();

    private int passengerTripId;
}
