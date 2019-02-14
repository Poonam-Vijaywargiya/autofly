package com.autofly.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autofly.repository.model.DriverZoneMap;

@Repository
public interface DriverZoneMapRepository extends JpaRepository<DriverZoneMap, Integer>{
	
}
