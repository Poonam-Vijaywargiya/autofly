package com.autofly.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.autofly.model.HotspotZone;
import com.autofly.repository.model.Hotspot;

@Component
public class HotspotZones {
	
	private static Map<HotspotZone, List<Integer>> zones = new HashMap<>();


//    ID  	LAT  	LNG  	NAME
//1	12.983784	77.752245	Hope Farm Circle
//2	12.98747	77.736142	ITPL Mall
//3	12.987752	77.736174	Opp. ITPL Mall
//4	12.987794	77.731752	Big Bazzar ITPL
//5	12.988157	77.73142	Big Bazzar ITPL PSN
//6	12.988281	77.731701	Big Bazzar ITPL(Towards Hope Farm)
//7	12.988932	77.727979	PSN
//8	12.988777	77.727948	Opp. PSN
//9	12.991906	77.715718	Hoodi Circle -> Graphite
//10	12.992353	77.716387	Hoodi Circle -> PSN
//11	12.980107	77.708892	Graphite (Tds Hoodi, Brigade, BF)
//12	12.977925	77.70978	Graphite Circle <- SAP
//13	12.978113	77.70976	Graphite India(Towards SAP)
//14	12.966284	77.718121	Brookefield Mall
//15	12.966313	77.718243	Opp. Brookefield Mall
//16	12.95594	77.714781	Kundanahalli Circle
//17	12.979799	77.727486	Inorbit Mall
//18	12.979825	77.72717	Opp. Inorbit Mall
//19	12.976648	77.726731	Vydehi Hospital
//20	12.976419	77.726719	Opp Vydehi Hospital
//21	12.977832	77.714456	SAP Labs
//22	12.977615	77.7144	Opp SAP Labs
//23	12.993053	77.703638	Brigade Metropolis
//24	12.993511	77.703804	Opp. Brigade Metropolis
//25	12.995539	77.695586	Phoenix Mall
//26	12.995322	77.695527	Opp. Phoenix Mall
//27	12.997936	77.689346	HP

	private HotspotZones() {

        zones.put(new HotspotZone(1), Arrays.asList(1, 2, 5, 8, 7, 6, 3, 1));
        zones.put(new HotspotZone(2), Arrays.asList(8, 9, 23, 26, 25, 24, 10, 7, 8));
        zones.put(new HotspotZone(3), Arrays.asList(6, 17, 20, 19, 18, 5, 6));
        zones.put(new HotspotZone(4), Arrays.asList(19, 20,  22, 12, 15, 16, 14, 13, 21, 19));
    }


	public static HotspotZones singleInstance = new HotspotZones();
	
	public Map<HotspotZone, List<Integer>> getZones() {
		Map<HotspotZone, List<Integer>> map = new HashMap<>();
		map = zones;
		return map;
	}

}
