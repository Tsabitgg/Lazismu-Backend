package com.ict.careus.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.user.SubAdmin;
import com.ict.careus.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long submissionId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "campaignId")
    private Campaign campaign;

    private double submissionAmount;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date submissionDate;

    private Date approvedDate = null;

    private boolean approved;
}
