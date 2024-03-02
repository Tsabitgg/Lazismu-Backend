package com.ict.careus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int donationId;

    @ManyToOne
    @JoinColumn(name = "campaignId")
    private Campaign campaign;

    @Column(length = 20)
    private String name;

    @Column(length = 15)
    private String noPhone;
    private double amount;
    private String message;
    private Date donationDate;
    private boolean success;
}
