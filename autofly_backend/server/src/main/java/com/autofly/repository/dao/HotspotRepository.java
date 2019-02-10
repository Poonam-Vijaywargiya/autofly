package com.autofly.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autofly.repository.model.Hotspot;

public interface HotspotRepository extends JpaRepository<Hotspot, Integer>{

	Optional<Hotspot> findById(int id);
}
