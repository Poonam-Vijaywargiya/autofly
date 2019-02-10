package com.autofly.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autofly.repository.model.Hotspot;

public interface HotspotRepository extends JpaRepository<Hotspot, Integer>{
	
}
