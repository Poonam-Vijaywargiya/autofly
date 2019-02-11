package com.autofly.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.autofly.model.HotspotZone;

public class HotspotZones {
	
	private static Map<HotspotZone, List<Integer>> zones = new HashMap<>();
	
//	ID  	LAT  	LNG  	NAME  
//	1	12.98747	77.736464	ITPL Mall
//	2	12.98957	77.727983	PSN
//	3	12.992353	77.716387	Hoodi Circle-PSN
//	4	12.992443	77.716035	Hoodi Circle-Phoenix
//	5	12.99219	77.716322	Hoodi Circle-Graphite
//	6	12.993053	77.703638	Brigade Metropolis
//	7	12.997361	77.69663	Phoenix Mall
//	8	12.979568	77.728424	Inorbit Mall
//	9	12.97717	77.726652	Vydehi Hospital
//	10	12.977921	77.714472	SAP Labs
//	11	12.977638	77.709937	Graphite India - SAP
//	12	12.978025	77.70954	Graphite India - Brookefield
//	13	12.966346	77.717892	Brookefield Mall
//	14	12.956136	77.715723	Kundanahalli Gate Signal
	
	private HotspotZones() {
		
		//Making the median hotspot part of the map key
		zones.put(new HotspotZone(1, 2), Arrays.asList(1, 2, 3));
		zones.put(new HotspotZone(2, 6), Arrays.asList(4, 6, 7));
		zones.put(new HotspotZone(3, 12), Arrays.asList(5, 12, 13));
		zones.put(new HotspotZone(4, 8), Arrays.asList(2, 8, 9));
		zones.put(new HotspotZone(5, 10), Arrays.asList(9, 10, 11));
		zones.put(new HotspotZone(7, 13), Arrays.asList(12, 13, 14));
	}
	
	public static HotspotZones singleInstance = new HotspotZones();
	
	public Map<HotspotZone, List<Integer>> getZones() {
		Map<HotspotZone, List<Integer>> map = new HashMap<>();
		map = zones;
		return map;
	}

}
