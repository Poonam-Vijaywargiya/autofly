package com.autofly.repository.model;

import java.util.List;
import java.util.Objects;

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
//@EqualsAndHashCode
@Entity
public class AutoDriver {

	@Id
	private int userId;
	
	private String name;
	private String mobNo;
	private String autoVehicleNo;
	private Double walletBalance;
	private Double rating;
	
	private Integer noOfPassengersBoarded;
	
	private Boolean onRide;
	private Boolean waiting;
	
	private Integer currentHotspot;
	private Integer currentZone;
	
	private transient List<Integer> route;

	private transient List<Integer> boardedPassengers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoDriver that = (AutoDriver) o;
        return getUserId() == that.getUserId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUserId());
    }
}
