package com.autofly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.FindHotspotZoneRequest;
import com.autofly.model.FindHotspotZoneResponse;
import com.autofly.model.LoginRequest;
import com.autofly.model.LoginResponse;
import com.autofly.repository.dao.UserRepository;
import com.autofly.repository.model.User;

@Component
public class AutoflyServiceImpl implements AutoflyService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private FindHotspotZoneService findHotspot;
	
	@Override
	public LoginResponse loginService(LoginRequest request) {
		
		LoginResponse response = new LoginResponse();
		response.setMessage("Login Failed");
		User user = userRepo.findByEmailIdAndPassword(request.getEmailId(), request.getPassword());
		if(null != user) {
			response.setName(user.getName());
			response.setMobNo(user.getMobNo());
			response.setUserType(user.getUserType());
			response.setSuccess(true);
			response.setMessage("Login Successful");
		}
		
		return response;
	}

	@Override
	public FindHotspotZoneResponse findHotspotZoneService(FindHotspotZoneRequest request) {
		
		FindHotspotZoneResponse response = findHotspot.findHotspotZone(request);
		return response;
	}
	
}
