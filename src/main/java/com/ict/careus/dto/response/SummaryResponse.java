package com.ict.careus.dto.response;

import lombok.Data;

@Data
public class SummaryResponse {
    private Double totalDistributionAmount;
    private long totalDistributionReceiver;
    private Double totalTransactionAmount;
    private long totalUser;
}
