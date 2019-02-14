package com.autofly.service;


import com.autofly.model.WalletRequest;
import com.autofly.model.WalletResponse;
import com.autofly.repository.dao.PassengerRepository;
import com.autofly.repository.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PassengerService {


    @Autowired
    PassengerRepository passengerRepo;

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
}
