package com.autofly.model;

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
public class AssignAutoRequest {
	
	private int driverId;
	private boolean confirmed;
	private Integer assignedZone;
	private Integer assignedHotspot;
}
