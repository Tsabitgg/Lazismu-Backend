package com.ict.careus.model.ziswaf;

import com.ict.careus.enumeration.WakafCategory;
import com.ict.careus.enumeration.ZakatCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wakaf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wakafId;
    private String wakafCode;

    @Enumerated(EnumType.STRING)
    private WakafCategory wakafCategory;
    private double amount;
}
