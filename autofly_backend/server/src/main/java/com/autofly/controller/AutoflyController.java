package com.autofly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autofly.model.LoginRequest;
import com.autofly.model.ServiceResponse;
import com.autofly.service.AutoflyService;

@RestController
public class AutoflyController {
	
	@Autowired
	private AutoflyService service;

	@PostMapping("/login")
    //@CrossOrigin(origins = {"http://localhost:8100","http://localhost:8080"})
    public ResponseEntity<ServiceResponse> login(@RequestBody LoginRequest request) {
		
		ServiceResponse response = service.loginService(request);
		return new ResponseEntity<>(response, HttpStatus.OK); 
    }
}
