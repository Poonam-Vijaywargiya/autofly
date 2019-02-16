package com.autofly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.AddPassengerRequest;
import com.autofly.model.AddPassengerResponse;
import com.autofly.model.AssignAutoRequest;
import com.autofly.model.ConfirmTripRequest;
import com.autofly.model.ConfirmTripResponse;
import com.autofly.model.EndRideRequest;
import com.autofly.model.EndRideResponse;
import com.autofly.model.EndTripRequest;
import com.autofly.model.EndTripResponse;
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
import com.autofly.repository.dao.AutoDriverRepository;
import com.autofly.repository.dao.PassengerRepository;
import com.autofly.repository.dao.UserRepository;
import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.Passenger;
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

	
	@Autowired
	private FindAutoService findAutoService;
	
	@Autowired
	private StartRideService startRideService;

    @Autowired
    private EndRideService endRideService ;


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
	
	@Override
	public Boolean assignAutoService(AssignAutoRequest request) {
		
		return(findHotspot.assignAuto(request));

	}

    @Override
    public ConfirmTripResponse confirmPassengerTrip(ConfirmTripRequest request){
        ConfirmTripResponse response = passengerService.confirmPassengerTrip(request);
        return response;
    }

	@Override
	public FindAutoResponse findAutoService(FindAutoRequest request) {
		FindAutoResponse response = findAutoService.findAuto(request);
		return response;
	}

	@Override
	public AddPassengerResponse addPassenger(AddPassengerRequest request) {
		AddPassengerResponse response = passengerService.addPassenger(request);
		return response;
	}

	@Override
	public StartRideResponse startRideService(StartRideRequest request) {
		StartRideResponse response = startRideService.startRide(request);
		return response;
	}

	@Override
	public EndTripResponse endTrip(EndTripRequest request) {
		return passengerService.endTrip(request);
	}

    public EndRideResponse endRideService(EndRideRequest request) {
        EndRideResponse response = endRideService.endRide(request);
        return response;
    }

}
