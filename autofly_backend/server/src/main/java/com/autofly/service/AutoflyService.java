package com.autofly.service;

import com.autofly.model.*;
import org.springframework.stereotype.Service;

@Service
public interface AutoflyService {

	public LoginResponse loginService(LoginRequest request);
	
	//Driver Related
	public FindHotspotZoneResponse findHotspotZoneService(FindHotspotZoneRequest request);

	//Passenger Related
	public RouteResponse getRouteService(RouteRequest request);
}
