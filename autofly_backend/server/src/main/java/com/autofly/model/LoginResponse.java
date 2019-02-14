package com.autofly.model;

import com.autofly.repository.model.AutoDriver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

	private boolean success;
	private String message;
	private String userType;
	private AutoDriver driver;
	
}
