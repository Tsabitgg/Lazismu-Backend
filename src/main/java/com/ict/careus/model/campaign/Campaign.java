package com.ict.careus.model.campaign;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    private String campaignName;

    private String campaignCode;
    private String campaignImage;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
    private double targetAmount;
    private double currentAmount;
    private String vaNumber;
    private String creator;
    private double distribution;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(columnDefinition = "BOOLEAN")
    private boolean active;

    private String generateLink;

    @Column(columnDefinition = "BOOLEAN")
    private boolean approved;
}
