package com.autofly.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	private String emailId;
	private String password;

	@Override
	public String toString() {
		return "LoginRequest{" +
				"emailId='" + emailId + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
