package com.autofly.service;


import com.autofly.model.WalletRequest;
import com.autofly.model.WalletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PassengerService {



    public WalletResponse checkWalletBalance(WalletRequest request) {

        WalletResponse response = new WalletResponse();

        return response;
    }
}
