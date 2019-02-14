package com.autofly.model;

import com.autofly.repository.model.Hotspot;
import lombok.*;
import com.google.maps.model.DirectionsResult;


import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@NoArgsConstructor
public class RouteResponse {
    private LatLng source;
    private LatLng destination;
    private int passengerId;
    private LocalDateTime departureTime;
    private List<Hotspot> route;
    private boolean success;
    private List<LatLng> walkFromSource;
    private List<LatLng> walkToDestination;
    private double fare;
}
