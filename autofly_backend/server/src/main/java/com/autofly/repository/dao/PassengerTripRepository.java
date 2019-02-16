package com.autofly.repository.dao;


import com.autofly.repository.model.PassengerTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerTripRepository extends JpaRepository<PassengerTrip, Integer> {

    PassengerTrip findByPassengerId(int passengerId);

    PassengerTrip findByPassengerIdAndTripStatus(int passengerId, String tripStatus);

    PassengerTrip findByIdAndTripStatus(int tripId, String tripStatus);
    

}
