package com.ict.careus.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.user.User;
import com.ict.careus.model.ziswaf.Infak;
import com.ict.careus.model.ziswaf.Wakaf;
import com.ict.careus.model.ziswaf.Zakat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long billingId;

    @Column(nullable = false, length = 8)
    private String createdTime;

    @PrePersist
    protected void onCreate() {
        this.createdTime = new SimpleDateFormat("HHmmssSS").format(new Date()).substring(0, 8);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Column(length = 20)
    private String username;

    @Column(length = 15)
    private String phoneNumber;

    private double billingAmount;
    private String message;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime billingDate;

    private long vaNumber;
    private String method;

    @Column(name = "transactionQrId")
    private long transactionQrId;
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
