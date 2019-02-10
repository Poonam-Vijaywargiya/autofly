package com.autofly.model;

public class Driver extends UserProfile {
	
	private LatLng currentLocation;
	private LatLng destLocation;
	private String autoVehicleNo;
	
	public LatLng getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(LatLng currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	public LatLng getDestLocation() {
		return destLocation;
	}
	public void setDestLocation(LatLng destLocation) {
		this.destLocation = destLocation;
	}
	
	public String getAutoVehicleNo() {
		return autoVehicleNo;
	}
	public void setAutoVehicleNo(String autoVehicleNo) {
		this.autoVehicleNo = autoVehicleNo;
	}
}
