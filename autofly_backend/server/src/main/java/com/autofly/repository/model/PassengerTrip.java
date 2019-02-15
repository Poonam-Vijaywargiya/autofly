package com.autofly.repository.model;


import com.autofly.model.LatLng;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class PassengerTrip {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int passengerId;
    private String sourceLocation;
    private String destinationLocation;
    @Column(length = 2000)
    private String route;
    private double fare;

    // Confirmed, Ongoing, Completed
    private String tripStatus;

}
