package com.autofly.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autofly.model.LoginRequest;
import com.autofly.model.ServiceResponse;
import com.autofly.service.AutoflyService;

import java.io.IOException;

@RestController
public class AutoflyController {
	
	@Autowired
	private AutoflyService service;

	@Autowired
	private GeoApiContext getGeoApiContext;

	@PostMapping("/login")
    //@CrossOrigin(origins = {"http://localhost:8100","http://localhost:8080"})
    public ResponseEntity<ServiceResponse> login(@RequestBody LoginRequest request) {
		
		ServiceResponse response = service.loginService(request);

		return new ResponseEntity<>(response, HttpStatus.OK); 
    }

    @PostMapping("/findgeocode")
    public ResponseEntity<GeocodingResult[]> getGeoCode(@RequestBody String currentAddress) {

        GeocodingResult[] results = new GeocodingResult[0];

        String address = !currentAddress.isEmpty()? currentAddress : "1600 Amphitheatre Parkway Mountain View, CA 94043";
        try {

            results = GeocodingApi.geocode(getGeoApiContext,address).await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println(gson.toJson(results[0].addressComponents));

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    }
