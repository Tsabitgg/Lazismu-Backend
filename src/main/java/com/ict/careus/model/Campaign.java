package com.ict.careus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int campaignId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String campaignName;
    private String campaignCode;
    private String campaignImage;
    private String description;
    private String location;
    private double targetAmount;
    private double currentAmount;
    private String vaNumber;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    private boolean active;

    private String generatelink;
}
