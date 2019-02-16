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
	
	@Autowired
	private HotspotZones hotspotZones;
	
	private static final Date CURR_DATE = Date.valueOf(LocalDate.now());
	
	private static final String RIDE_WAITING = "Waiting";
	
	private HotspotZones zoneInstance = HotspotZones.singleInstance;

	public FindHotspotZoneResponse findHotspotZone(FindHotspotZoneRequest request) {
	
		FindHotspotZoneResponse response = new FindHotspotZoneResponse();
		
		LatLng autoLocation = new LatLng(request.getDriverLat(), request.getDriverLng());
		
		Hotspot nearestHotspot = this.findClosestHotspot(autoLocation);

	    response.setAssignedHotspot(nearestHotspot);
		response.setAssignedZone(nearestHotspot.getCurrentZoneId());

		Map<HotspotZone, List<Integer>> zones = HotspotZones.singleInstance.getZones();
		
		response.setHotspotLists(
				zones.get(new HotspotZone(nearestHotspot.getCurrentZoneId()))
								   .stream()
								   .map(hotspot -> hotspotRepo.findById(hotspot).orElse(null))
								   .collect(Collectors.toList())
		);

		if(!response.getHotspotLists().isEmpty()) {
			response.setSuccess(true);
		}
		
		return response;
	}
	
	 private Hotspot findClosestHotspot(LatLng location) {

	        Map<HotspotZone, List<Integer>> zones = zoneInstance.getZones();

	        long minDistance = Long.MAX_VALUE;

	        Hotspot closestHotspot = null;
	        
	        for(HotspotZone zone : zones.keySet()) {

	            List<Integer> hotspotIdList = zones.get(zone);

	            for (Integer hotspotId: hotspotIdList){
	                Hotspot hotspot = hotspotRepo.findById(hotspotId).orElse(new Hotspot());
	                int distance = this.distanceInKms(
	                		mapUtils.getDriveDist(location, new LatLng(hotspot.getLat(), hotspot.getLng())));
	                if(distance < minDistance) {
	                    minDistance = distance;
	                    closestHotspot = hotspot;
	                    closestHotspot.setCurrentZoneId(zone.getZoneId());
	                }
	            }
	        }
	        return closestHotspot;
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
		
		return true;
	}

}
