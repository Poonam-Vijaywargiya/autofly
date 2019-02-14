package com.autofly.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.autofly.model.HotspotZone;

@Component
public class HotspotZones {
	
	private static Map<HotspotZone, List<Integer>> zones = new HashMap<>();

//	ID  	LAT  	LNG  	NAME  
//	1	12.98747	77.736142	ITPL Mall
//	2	12.987752	77.736174	Opp. ITPL Mall
//	3	12.989026	77.727833	PSN
//	4	12.988733	77.727972	Opp. PSN
//	5	12.992067	77.715976	Hoodi Circle -> Graphite
//	6	12.985483	77.708167	Graphite Signal
//	7	12.966114	77.718365	Opp. Brookefield Mall
//	8	12.958361	77.715753	Kundanahalli Hypercity
//	9	12.956235	77.714919	Kundanahalli Gate Signal1
//	10	12.956217	77.714774	Kundanahalli Gate Signal2
//	11	12.958416	77.715613	Opp. Kundanahalli Hypercity
//	12	12.966129	77.717946	Brookefield Mall
//	13	12.977912	77.714309	SAP Labs
//	14	12.976765	77.726664	Vydehi Hospital Bus-stop
//	15	12.979576	77.727129	Opp. Inorbit Mall
//	16	12.992309	77.716594	Hoodi Circle -> PSN
//	17	12.995288	77.69566	Opp. Phoenix Mall
//	18	12.995549	77.69566	Phoenix Mall
//	19	12.99395	77.702239	Opp. Brigade Metropolis
//	20	12.993772	77.702057	Brigade Metropolis
	
	private HotspotZones() {
		
		//Making the median hotspot part of the map key
		zones.put(new HotspotZone(1, 4), Arrays.asList(1, 4, 5));
		zones.put(new HotspotZone(2, 6), Arrays.asList(5, 6, 7));
		zones.put(new HotspotZone(3, 8), Arrays.asList(7, 8, 9));
		zones.put(new HotspotZone(4, 11), Arrays.asList(10, 11, 12));
		zones.put(new HotspotZone(5, 13), Arrays.asList(12, 13, 14));
		zones.put(new HotspotZone(6, 15), Arrays.asList(14, 15, 4));
		
		zones.put(new HotspotZone(7, 19), Arrays.asList(18, 19, 16));
		zones.put(new HotspotZone(8, 3), Arrays.asList(16, 3, 2));
		zones.put(new HotspotZone(9, 20), Arrays.asList(5, 20, 17));

	}
	
	
	
	public static HotspotZones singleInstance = new HotspotZones();
	
	public Map<HotspotZone, List<Integer>> getZones() {
		Map<HotspotZone, List<Integer>> map = new HashMap<>();
		map = zones;
		return map;
	}

}
