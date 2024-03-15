package com.ict.careus.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class TransactionRequest {
    private String username;
    private String phoneNumber;
    private double transactionAmount;
    private String message;
    private Set<String> role;
}
