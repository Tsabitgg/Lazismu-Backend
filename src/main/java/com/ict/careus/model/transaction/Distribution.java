package com.ict.careus.model.transaction;

import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.ziswaf.Infak;
import com.ict.careus.model.ziswaf.Wakaf;
import com.ict.careus.model.ziswaf.Zakat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Distribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double distributionAmount;

    private Date distributionDate;

    private String receiver;

    private String image;

    private String description;

    private boolean success;

    @Column(length = 10)
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
