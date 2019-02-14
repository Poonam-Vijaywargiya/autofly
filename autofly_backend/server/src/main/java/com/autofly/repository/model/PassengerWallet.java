package com.autofly.repository.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class PassengerWallet {

    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(generator="gen")
    @GenericGenerator(name="gen", strategy="foreign", parameters= @org.hibernate.annotations.Parameter(name="property", value="wallet"))
    private int id;

    private double creditBalance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(double creditBalance) {
        this.creditBalance = creditBalance;
    }
}
