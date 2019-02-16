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
public class AddPassengerRequest {
    private int passengerId;
    private int rideId;
    private int passengerTripId;
}
