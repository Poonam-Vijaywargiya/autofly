package com.autofly.model;

import java.util.List;

import com.autofly.repository.model.Hotspot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FindHotspotZoneResponse {
	
	private boolean success;
	private HotspotZone assignedZone;
	private List<Hotspot> hotspotLists;
	
}
