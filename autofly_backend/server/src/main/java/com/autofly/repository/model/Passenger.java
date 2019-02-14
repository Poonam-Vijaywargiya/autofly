package com.autofly.repository.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Passenger {


    @Id
    private int userId;

    private String name;
    private String mobNo;
    private double walletBalance;
    private double rating;
}
