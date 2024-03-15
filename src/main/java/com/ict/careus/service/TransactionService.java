package com.ict.careus.service;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.model.transaction.Transaction;

public interface TransactionService {

    Transaction createTransaction(String transactionType, String code, TransactionRequest transactionRequest);
}
