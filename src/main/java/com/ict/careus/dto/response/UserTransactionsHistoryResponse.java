package com.ict.careus.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserTransactionsHistoryResponse {
    private String username;
    private String type;
    private String transactionName;
    private double transactionAmount;
    private String message;
    private LocalDate transactionDate;
    private boolean success;
}
