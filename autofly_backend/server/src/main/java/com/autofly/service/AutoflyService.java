package com.autofly.service;

import com.autofly.model.*;
import org.springframework.stereotype.Service;

@Service
public interface AutoflyService {

	public LoginResponse loginService(LoginRequest request);
	
	//Driver Related
	public FindHotspotZoneResponse findHotspotZoneService(FindHotspotZoneRequest request);
	
	public Boolean assignAutoService(AssignAutoRequest request);
	
	public FindAutoResponse findAutoService(FindAutoRequest request);
	
	//Passenger Related
	public RouteResponse getRouteService(RouteRequest request);

	public WalletResponse checkWalletBalance(WalletRequest request);

	public ConfirmTripResponse confirmPassengerTrip(ConfirmTripRequest request);

}
