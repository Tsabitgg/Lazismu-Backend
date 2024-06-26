package com.ict.careus.dto.response;

import com.ict.careus.enumeration.InfakCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmilInfakResponse {
    private long infakId;
    private InfakCategory infakCategory;
    private String infakCode;
    private double amount;
    private double amil;
}
