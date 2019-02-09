package com.autofly.service;

import org.springframework.stereotype.Service;

import com.autofly.model.LoginRequest;
import com.autofly.model.ServiceResponse;

@Service
public interface AutoflyService {

	public ServiceResponse loginService(LoginRequest request);
}
