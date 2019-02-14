package com.autofly.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autofly.repository.model.AutoDriver;

@Repository
public interface AutoDriverRepository extends JpaRepository<AutoDriver, Integer>{
	
	AutoDriver findByUserId(int userId);
}
