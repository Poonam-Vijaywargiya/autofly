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
public class FindHotspotZoneRequest {

	private int driverId;
	private double driverLat;
	private double driverLng;
	
	
}
