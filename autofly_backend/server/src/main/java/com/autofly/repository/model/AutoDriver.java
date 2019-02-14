package com.autofly.repository.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class AutoDriver {

	@Id
	private int userId;
	
	private String name;
	private String mobNo;
	private String autoVehicleNo;
	private double walletBalance;
	private double rating;
	
}
