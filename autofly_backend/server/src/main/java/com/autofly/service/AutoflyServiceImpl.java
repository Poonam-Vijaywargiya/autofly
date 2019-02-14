package com.autofly.service;

import com.autofly.model.*;
import com.autofly.repository.dao.PassengerRepository;
import com.autofly.repository.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.repository.dao.AutoDriverRepository;
import com.autofly.repository.dao.UserRepository;
import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.User;

@Component
public class AutoflyServiceImpl implements AutoflyService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    private AutoDriverRepository driverRepo;

    @Autowired
    private PassengerRepository passengerRepo;
	
	@Autowired
	private FindHotspotZoneService findHotspot;

	@Autowired
	private RouteService routeService;

	@Autowired
	private PassengerService passengerService;
	
	@Override
	public LoginResponse loginService(LoginRequest request) {
		
		LoginResponse response = new LoginResponse();
		response.setMessage("Login Failed");
		User user = userRepo.findByEmailIdAndPassword(request.getEmailId(), request.getPassword());
		if(null != user && "D".equalsIgnoreCase(user.getUserType())) {
			AutoDriver driver = driverRepo.findByUserId(user.getUserId());
			response.setDriver(driver);
			response.setUserType("D");
			response.setSuccess(true);
			response.setMessage("Login Successful");
		}
		else if (null != user && "P".equalsIgnoreCase(user.getUserType())) {
            Passenger passenger = passengerRepo.findByUserId(user.getUserId());
            response.setPassenger(passenger);
            response.setUserType("P");
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

	@Override
	public RouteResponse getRouteService(RouteRequest request) {

		RouteResponse response = routeService.getRouteForPassenger(request);
		return response;
	}

	@Override
	public WalletResponse checkWalletBalance(WalletRequest request){
		WalletResponse response = passengerService.checkWalletBalance(request);
		return response;
	}
	
}
