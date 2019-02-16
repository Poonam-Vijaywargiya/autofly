package com.autofly.model;

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
public class FindAutoRequest {
	
	private int zoneId;
	private int fromHotspotId;
	private int toHotspotId;
	
	private int passengerId;

	private int passengerTripId;
}
