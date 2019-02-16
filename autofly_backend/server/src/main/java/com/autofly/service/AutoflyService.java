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
	
	public StartRideResponse startRideService(StartRideRequest request);

	public EndRideResponse endRideService(EndRideRequest request);
	
	//Passenger Related
	public RouteResponse getRouteService(RouteRequest request);

	public WalletResponse checkWalletBalance(WalletRequest request);

	public ConfirmTripResponse confirmPassengerTrip(ConfirmTripRequest request);

	public AddPassengerResponse addPassenger(AddPassengerRequest request);

	public EndTripResponse endTrip(EndTripRequest request);

	public OffDutyResponse offDuty(OffDutyRequest request);

}
