package com.autofly.model;

import java.util.List;

import com.autofly.repository.model.Passenger;
import com.autofly.repository.model.Ride;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StartRideResponse {

	private boolean success;
	private String rideStatus;
	private List<Passenger> passengers;
	private List<Ride> rides;
	
}
