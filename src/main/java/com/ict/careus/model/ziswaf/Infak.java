package com.ict.careus.model.ziswaf;

import com.ict.careus.enumeration.InfakCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Infak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int infakId;
    private String infakCode;

    @Enumerated(EnumType.STRING)
    private InfakCategory infakCategory;
    private double amount;

}
