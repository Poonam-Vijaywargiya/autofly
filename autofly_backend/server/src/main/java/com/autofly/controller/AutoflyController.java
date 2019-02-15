package com.autofly.controller;


import com.autofly.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autofly.service.AutoflyService;

@RestController
@RequestMapping("/autofly")
public class AutoflyController {
	
	@Autowired
	private AutoflyService service;


	@PostMapping("/login")
    @CrossOrigin(origins = {"http://localhost:8181","http://localhost:8080", "http://localhost:8100"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		
		LoginResponse response = service.loginService(request);

		return new ResponseEntity<>(response, HttpStatus.OK); 
    }

    
    @PostMapping("/findHotspotZone")
    //CrossOrigin(origins = {"http://localhost:8181","http://localhost:8080"})
    public ResponseEntity<FindHotspotZoneResponse> findHotspotZone(@RequestBody FindHotspotZoneRequest request) {
		
    	FindHotspotZoneResponse response = service.findHotspotZoneService(request);

		return new ResponseEntity<>(response, HttpStatus.OK); 
    }
    
    @PostMapping("/assignAuto")
    //CrossOrigin(origins = {"http://localhost:8181","http://localhost:8080"})
    public ResponseEntity<Boolean> assignAuto(@RequestBody AssignAutoRequest request) {
		
    	Boolean response = service.assignAutoService(request);

		return new ResponseEntity<>(response, HttpStatus.OK); 
    }

	@PostMapping("/getRoute")
	//CrossOrigin(origins = {"http://localhost:8181","http://localhost:8080"})
	public ResponseEntity<RouteResponse> getRoute(@RequestBody RouteRequest request) {

		RouteResponse response = service.getRouteService(request);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/checkWalletBalance")
	//CrossOrigin(origins = {"http://localhost:8181","http://localhost:8080"})
	public ResponseEntity<WalletResponse> checkWalletBalance(@RequestBody WalletRequest request) {

		WalletResponse response = service.checkWalletBalance(request);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
}
