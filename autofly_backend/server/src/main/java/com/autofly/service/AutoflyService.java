package com.autofly.service;

import org.springframework.stereotype.Service;

import com.autofly.model.FindHotspotZoneRequest;
import com.autofly.model.FindHotspotZoneResponse;
import com.autofly.model.LoginRequest;
import com.autofly.model.LoginResponse;

@Service
public interface AutoflyService {

	public LoginResponse loginService(LoginRequest request);
	
	//Driver Related
	public FindHotspotZoneResponse findHotspotZoneService(FindHotspotZoneRequest request);
}
