package com.autofly.service;


import com.autofly.model.ConfirmTripRequest;
import com.autofly.model.ConfirmTripResponse;
import com.autofly.model.WalletRequest;
import com.autofly.model.WalletResponse;
import com.autofly.repository.dao.PassengerRepository;
import com.autofly.repository.dao.PassengerTripRepository;
import com.autofly.repository.model.Passenger;
import com.autofly.repository.model.PassengerTrip;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PassengerService {


    @Autowired
    PassengerRepository passengerRepo;

    PassengerTripRepository passengerTripRepo;

    public WalletResponse checkWalletBalance(WalletRequest request) {

        WalletResponse response = new WalletResponse();

        Passenger passenger = passengerRepo.findByUserId(request.getPassengerId());

        boolean isCreditAvailable  = false;

        response.setPassengerId(request.getPassengerId());
        response.setFare(request.getFare());
            if(passenger != null && passenger.getWalletBalance() >= request.getFare()){
                isCreditAvailable = true;
                response.setCreditAvailable(isCreditAvailable);
            }
            else {
                response.setCreditAvailable(isCreditAvailable);
            }

        return response;
    }

    public ConfirmTripResponse confirmPassengerTrip(ConfirmTripRequest request){

        ConfirmTripResponse  response= new ConfirmTripResponse();

        int passengerId = request.getPassengerId();

        Gson gson = new Gson();

        Passenger passenger = passengerRepo.findByUserId(passengerId);

        if(passenger != null){
            String sourceLocation = gson.toJson(request.getSourceLocation());
            String destLocation = gson.toJson(request.getDestinationLocation());
            String route  = gson.toJson(request.getRoute());

            PassengerTrip passengerTrip  = new PassengerTrip();
            passengerTrip.setPassengerId(passengerId);
            passengerTrip.setSourceLocation(sourceLocation);
            passengerTrip.setDestinationLocation(destLocation);
            passengerTrip.setRoute(route);
            passengerTrip.setFare(request.getFare());
            passengerTrip.setTripStatus("CONFIRMED");

            PassengerTrip savedPassengerTrip = passengerTripRepo.save(passengerTrip);

            if(savedPassengerTrip != null) {
                response.setPassengerTrip(savedPassengerTrip);
                response.setSuccess(true);
            }
            
        }

        return response;
    }
}
