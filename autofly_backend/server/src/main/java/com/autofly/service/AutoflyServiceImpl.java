package com.autofly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.LoginRequest;
import com.autofly.model.ServiceResponse;
import com.autofly.repository.dao.UserRepository;
import com.autofly.repository.model.User;

@Component
public class AutoflyServiceImpl implements AutoflyService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public ServiceResponse loginService(LoginRequest request) {
		
		ServiceResponse response = new ServiceResponse();
		response.setMessage("Login Failed");
		User user = userRepo.findByEmailIdAndPassword(request.getEmailId(), request.getPassword());
		if(null != user) {
			response.setSuccess(true);
			response.setMessage("Login Successful");
		}
		
		return response;
	}
	
	
}
