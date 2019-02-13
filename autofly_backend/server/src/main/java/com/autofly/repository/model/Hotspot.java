package com.autofly.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Hotspot {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private double lat;
	private double lng;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

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
