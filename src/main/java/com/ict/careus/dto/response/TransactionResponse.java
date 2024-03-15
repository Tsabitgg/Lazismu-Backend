package com.ict.careus.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionResponse {
    private long transactionId;
    private String username;
    private String phoneNumber;
    private double transactionAmount;
    private String message;
    private Date transactionDate;
    private boolean success;
}
