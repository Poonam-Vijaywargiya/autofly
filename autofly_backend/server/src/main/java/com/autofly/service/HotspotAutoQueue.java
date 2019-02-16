package com.autofly.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.stereotype.Component;

import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.model.AutoDriver;
import com.autofly.repository.model.Hotspot;

@Component
public class HotspotAutoQueue {

	
	private static Map<Hotspot, PriorityQueue<AutoDriver>> hotspotAutoQueues = new HashMap<>();

	
	public HotspotAutoQueue(HotspotRepository hotspotRepo) {
		
		//Initialize the queues in each hotspot
		List<Hotspot> hotspots = hotspotRepo.findAll();
		
		hotspots.stream().forEach(h -> hotspotAutoQueues.put(h, new PriorityQueue<>(new AutoPriorityComparator())));
	}
	
	public boolean addAutoToHotspot(Hotspot h, AutoDriver a) {
		
		PriorityQueue<AutoDriver> que = hotspotAutoQueues.get(h);
		que.add(a);
		
		return hotspotAutoQueues.put(h, que) != null;
	}
	
	public AutoDriver removeAutoFromHotspot(Hotspot h) {
		
		PriorityQueue<AutoDriver> que = hotspotAutoQueues.get(h);
		return que.poll();
	}
	
	public AutoDriver getFirstAutoInQueue(Hotspot h) {
		PriorityQueue<AutoDriver> que = hotspotAutoQueues.get(h);
		return que.peek();
	}

	public boolean removeAutoFromHotspot(AutoDriver a) {
		boolean success = false;
		for ( PriorityQueue<AutoDriver> hotspotAutoQueue : hotspotAutoQueues.values()) {
			 success = hotspotAutoQueue.remove(a);
			if(success){
				return success;
			}
		}
		return success;
	}
	
}