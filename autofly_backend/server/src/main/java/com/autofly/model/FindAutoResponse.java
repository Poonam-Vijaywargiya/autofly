package com.autofly.model;

import com.autofly.repository.model.AutoDriver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FindAutoResponse {

	private boolean foundAuto;
	private AutoDriver driver;
	private int rideId;
	
}
