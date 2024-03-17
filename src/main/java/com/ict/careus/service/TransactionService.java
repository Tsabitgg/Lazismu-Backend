package com.ict.careus.service;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.dto.response.CampaignTransactionsHistoryResponse;
import com.ict.careus.dto.response.UserTransactionsHistoryResponse;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.model.user.User;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(String transactionType, String code, TransactionRequest transactionRequest);

    List<UserTransactionsHistoryResponse> getUserTransactionsHistory(User user);

    List<CampaignTransactionsHistoryResponse> getCampaignTransactionsHistory(Campaign campaign);
}
