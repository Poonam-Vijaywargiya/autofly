package com.autofly.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

	private boolean success;
	private String message;
	private String name;
	private String userType;
	private String mobNo;
	
}
