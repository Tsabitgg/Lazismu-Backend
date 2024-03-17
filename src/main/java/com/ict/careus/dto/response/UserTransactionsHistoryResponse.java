package com.ict.careus.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserTransactionsHistoryResponse {
    private String username;
    private String type;
    private String transactionName;
    private double transactionAmount;
    private String message;
    private Date transactionDate;
    private boolean success;
}
