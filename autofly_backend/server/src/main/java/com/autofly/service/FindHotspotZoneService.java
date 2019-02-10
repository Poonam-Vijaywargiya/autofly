package com.autofly.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.FindHotspotZoneRequest;
import com.autofly.model.FindHotspotZoneResponse;
import com.autofly.model.HotspotZone;
import com.autofly.model.LatLng;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.model.Hotspot;
import com.autofly.util.MapsUtil;

@Component
public class FindHotspotZoneService {

	@Autowired
	private MapsUtil mapUtils;
	
	@Autowired
	private HotspotRepository hotspotRepo;
	
	private HotspotZones zoneInstance = HotspotZones.singleInstance;

	public FindHotspotZoneResponse findHotspotZone(FindHotspotZoneRequest request) {
	
		FindHotspotZoneResponse response = new FindHotspotZoneResponse();
		
		LatLng autoLocation = new LatLng(request.getDriverLat(), request.getDriverLng());
		
		//Find the nearest zone based on prime hotspot locations		
		Map<HotspotZone, List<Integer>> zones = zoneInstance.getZones();
		
		List<HotspotZone> distances = new ArrayList<>(); 
		for(HotspotZone h : zones.keySet()) {
			
			Hotspot hotspot = hotspotRepo.findById(h.getPrimeHotspotId()).orElse(new Hotspot());
			LatLng hotspotLocation = new LatLng(hotspot.getLat(), hotspot.getLng());
			distances.add(new HotspotZone(h, distanceInKms(mapUtils.getDriveDist(autoLocation, hotspotLocation))));
			
		}
		
		HotspotZone minDistZone = distances.stream()
										.sorted((h1, h2) -> h1.getDistanceFrom() - h2.getDistanceFrom())
										.findFirst()
										.orElse(null);
		
		response.setAssignedZone(minDistZone);
		
		if(null != minDistZone) {

			response.setHotspotLists(
				zones.get(minDistZone)
				     .stream()
				     .map(hotspot -> hotspotRepo.findById(hotspot).orElse(null))
				     .collect(Collectors.toList())
			);
			
			if(!response.getHotspotLists().isEmpty()) {
				response.setSuccess(true);
			}
		}
		
		return response;
	}
	
	private int distanceInKms(long distance) {
		return (int) (distance>0 ? distance/1000 : 0);
	}

}
