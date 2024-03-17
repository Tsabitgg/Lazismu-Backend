package com.ict.careus.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class CampaignTransactionsHistoryResponse {
    private String username;
    private double transactionAmount;
    private String message;
    private Date transactionDate;
}
