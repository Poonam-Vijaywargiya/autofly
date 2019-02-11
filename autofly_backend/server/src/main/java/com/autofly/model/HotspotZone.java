package com.autofly.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"zoneId", "medianHotspotId"})
public class HotspotZone {

	private int zoneId;
	private int medianHotspotId;
	private int distanceFrom;
	
	public HotspotZone(int zoneId, int hotspot) {
		this.zoneId = zoneId;
		this.medianHotspotId = hotspot;
		this.distanceFrom = -1;
	}
	
	public HotspotZone(HotspotZone h, int distance) {
		this.zoneId = h.getZoneId();
		this.medianHotspotId = h.getMedianHotspotId();
		this.distanceFrom = distance;
	}

}
