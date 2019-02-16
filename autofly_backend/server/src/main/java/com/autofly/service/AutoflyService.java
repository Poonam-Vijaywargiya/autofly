package com.autofly.service;

import org.springframework.stereotype.Service;

import com.autofly.model.AssignAutoRequest;
import com.autofly.model.ConfirmTripRequest;
import com.autofly.model.ConfirmTripResponse;
import com.autofly.model.FindAutoRequest;
import com.autofly.model.FindAutoResponse;
import com.autofly.model.FindHotspotZoneRequest;
import com.autofly.model.FindHotspotZoneResponse;
import com.autofly.model.LoginRequest;
import com.autofly.model.LoginResponse;
import com.autofly.model.RouteRequest;
import com.autofly.model.RouteResponse;
import com.autofly.model.StartRideRequest;
import com.autofly.model.StartRideResponse;
import com.autofly.model.WalletRequest;
import com.autofly.model.WalletResponse;

@Service
public interface AutoflyService {

	public LoginResponse loginService(LoginRequest request);
	
	//Driver Related
	public FindHotspotZoneResponse findHotspotZoneService(FindHotspotZoneRequest request);
	
	public Boolean assignAutoService(AssignAutoRequest request);
	
	public FindAutoResponse findAutoService(FindAutoRequest request);
	
	public StartRideResponse startRideService(StartRideRequest request);
	
	//Passenger Related
	public RouteResponse getRouteService(RouteRequest request);

	public WalletResponse checkWalletBalance(WalletRequest request);

	public ConfirmTripResponse confirmPassengerTrip(ConfirmTripRequest request);

}
