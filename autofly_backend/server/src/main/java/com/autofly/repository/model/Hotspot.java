package com.autofly.repository.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotspot {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private double lat;
	private double lng;
	
	private Integer currentZoneId;
	
	private transient List<Integer> zoneId;
//	private transient boolean isDropLocation;
    private Boolean dropLocation;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotspot hotspot = (Hotspot) o;
        return getId() == hotspot.getId() &&
                Double.compare(hotspot.getLat(), getLat()) == 0 &&
                Double.compare(hotspot.getLng(), getLng()) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getLat(), getLng());
    }

    @Override
    public String toString() {
        return "Hotspot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
