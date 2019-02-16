package com.autofly.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndTripResponse {
	
	private boolean success;
	private Double updatedWalletBallance;
}
