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
public class PassengerTrip {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int passengerId;
    private LatLng sourceLocation;
    private LatLng destinationLocation;
    private String route;
    private double fare;
    private String tripStatus;

}
