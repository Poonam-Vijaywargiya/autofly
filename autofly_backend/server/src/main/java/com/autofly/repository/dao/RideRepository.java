package com.autofly.repository.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autofly.repository.model.Ride;

public interface RideRepository extends JpaRepository<Ride, Integer> {
	
	public List<Ride> findByAutoIdAndDutyDateAndFromHotspotAndRideStatusAndPassengerStatusAndZoneId
				(Integer autoId, Date dutyDate, Integer fromHotspot, String rideStatus, String passengerStatus, Integer zoneId);
	
}
