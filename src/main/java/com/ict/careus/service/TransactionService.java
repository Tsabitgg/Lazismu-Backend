package com.ict.careus.service;

import com.ict.careus.model.transaction.Transaction;

public interface TransactionService {

    Transaction createTransaction(String transactionType, String code, Transaction transaction);
}
