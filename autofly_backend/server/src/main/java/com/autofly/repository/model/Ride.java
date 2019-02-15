package com.autofly.repository.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class Ride {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rideId;
	
	private Date dutyDate;
	private Integer autoId;
	private Integer zoneId;
	private Integer fromHotspot;
	private Integer toHotspot;
	private Double earning;
	private Integer passengerId;
	private String rideStatus;
	private String passengerStatus;
	
}
