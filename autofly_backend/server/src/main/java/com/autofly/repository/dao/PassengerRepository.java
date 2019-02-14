package com.autofly.repository.dao;

import com.autofly.repository.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer>{

    Passenger findByUserId(int userId);
}
