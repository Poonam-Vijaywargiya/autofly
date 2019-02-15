package com.autofly.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.AssignAutoRequest;
import com.autofly.model.FindHotspotZoneRequest;
import com.autofly.model.FindHotspotZoneResponse;
import com.autofly.model.HotspotZone;
import com.autofly.model.LatLng;
import com.autofly.repository.dao.AutoDriverRepository;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.dao.RideRepository;
import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.Hotspot;
import com.autofly.repository.model.Ride;
import com.autofly.util.MapsUtil;

@Component
public class FindHotspotZoneService {

	@Autowired
	private MapsUtil mapUtils;
	
	@Autowired
	private HotspotRepository hotspotRepo;
	
	@Autowired
	private AutoDriverRepository driverRepo;
	
	@Autowired
	private HotspotAutoQueue hotspotAutoQueue;
	
	@Autowired
	private RideRepository rideRepo;
	
	private static final Date CURR_DATE = Date.valueOf(LocalDate.now());
	
	private static final String RIDE_WAITING = "Waiting";
	
	private HotspotZones zoneInstance = HotspotZones.singleInstance;

	public FindHotspotZoneResponse findHotspotZone(FindHotspotZoneRequest request) {
	
		FindHotspotZoneResponse response = new FindHotspotZoneResponse();
		
//		LatLng autoLocation = new LatLng(request.getDriverLat(), request.getDriverLng());
//
//
//		Map<HotspotZone, List<Integer>> zones = zoneInstance.getZones();
//
//		List<HotspotZone> distances = new ArrayList<>();
//		for(HotspotZone h : zones.keySet()) {
//
//			Hotspot hotspot = hotspotRepo.findById(h.getMedianHotspotId()).orElse(new Hotspot());
//			LatLng hotspotLocation = new LatLng(hotspot.getLat(), hotspot.getLng());
//			distances.add(new HotspotZone(h, distanceInKms(mapUtils.getDriveDist(autoLocation, hotspotLocation))));
//
//		}
//		//Find the nearest zone based on median hotspot locations
//		HotspotZone minDistZone = distances.stream()
//										.sorted((h1, h2) -> h1.getDistanceFrom() - h2.getDistanceFrom())
//										.findFirst()
//										.orElse(null);
//
//		//Find which edge hotspot to assign to auto
//		int edgeHotspotId1 = zones.get(minDistZone).get(0);
//		int edgeHotspotId2 = zones.get(minDistZone).get(2);
//
//		Hotspot edgeHotspot1 = hotspotRepo.findById(edgeHotspotId1).orElse(new Hotspot());
//		long dist1 = distanceInKms(mapUtils.getDriveDist(autoLocation, new LatLng(edgeHotspot1.getLat(), edgeHotspot1.getLng())));
//
//		Hotspot edgeHotspot2 = hotspotRepo.findById(edgeHotspotId2).orElse(new Hotspot());
//		long dist2 = distanceInKms(mapUtils.getDriveDist(autoLocation, new LatLng(edgeHotspot2.getLat(), edgeHotspot2.getLng())));
//
//	    response.setAssignedHotspot(dist1 < dist2 ? edgeHotspot1 : edgeHotspot2);
//		response.setAssignedZone(minDistZone.getZoneId());
//
//		if(null != minDistZone) {
//
//			response.setHotspotLists(
//				zones.get(minDistZone)
//				     .stream()
//				     .map(hotspot -> hotspotRepo.findById(hotspot).orElse(null))
//				     .collect(Collectors.toList())
//			);
//
//			if(!response.getHotspotLists().isEmpty()) {
//				response.setSuccess(true);
//			}
//		}
//
		return response;
	}
	
	private int distanceInKms(long distance) {
		return (int) (distance>0 ? distance/1000 : 0);
	}

	public boolean assignAuto(AssignAutoRequest request) {
		
		if(!request.isAvailable()) {
			return false;
		}
		
		AutoDriver auto =  driverRepo.findByUserId(request.getDriverId());
		auto.setCurrentZone(request.getAssignedZone());
		auto.setCurrentHotspot(request.getAssignedHotspot());
		auto.setNoOfPassengersBoarded(0);
		auto.setOnRide(false);
		auto.setWaiting(true);
		HotspotZone zone = new HotspotZone();
		zone.setZoneId(request.getAssignedZone());
		
		if(!zoneInstance.getZones().containsKey(zone)) {
			return false;
		}
		
		auto.setRoute(zoneInstance.getZones().get(zone));
		auto.setBoardedPassengers(new ArrayList<>());
		
		//Add to hotspot queue
		Hotspot h = hotspotRepo.findById(request.getAssignedHotspot()).orElse(null);
		if(!hotspotAutoQueue.addAutoToHotspot(h, auto)) {
			return false;
		}
		
		//Create ride
		Ride ride = new Ride();
		ride.setAutoId(auto.getUserId());
		ride.setDutyDate(CURR_DATE);
		ride.setEarning(0.0);
		ride.setFromHotspot(request.getAssignedHotspot());
		ride.setToHotspot(0);
		ride.setPassengerId(0);
		ride.setZoneId(request.getAssignedZone());
		ride.setRideStatus(RIDE_WAITING);
		if(null == rideRepo.save(ride)) {
			return false;
		}
		
		return true;
	}

}
