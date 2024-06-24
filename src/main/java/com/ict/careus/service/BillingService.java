package com.ict.careus.service;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.model.transaction.Billing;
import com.ict.careus.model.transaction.Transaction;
import org.apache.coyote.BadRequestException;

public interface BillingService {
    Billing createBilling(String transactionType, String code, TransactionRequest transactionRequest) throws BadRequestException;

    boolean getBillingSuccess(Long billingId);
}
