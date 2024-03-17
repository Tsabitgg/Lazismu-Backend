package com.ict.careus.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ict.careus.model.user.User;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.ziswaf.Infak;
import com.ict.careus.model.ziswaf.Wakaf;
import com.ict.careus.model.ziswaf.Zakat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    @Column(length = 20)
    private String username;

    @Column(length = 15)
    private String phoneNumber;
    private double transactionAmount;
    private String message;
    private Date transactionDate;
    private boolean success;

    @Column(length = 20)
    private String category;

    @ManyToOne
    @JoinColumn(name = "zakat_id")
    private Zakat zakat;

    @ManyToOne
    @JoinColumn(name = "infak_id")
    private Infak infak;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "wakaf_id")
    private Wakaf wakaf;
}

