package com.ict.careus.service;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.dto.response.CampaignTransactionsHistoryResponse;
import com.ict.careus.dto.response.TransactionResponse;
import com.ict.careus.dto.response.UserTransactionsHistoryResponse;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface TransactionService {

    Transaction createTransaction(String transactionType, String code, TransactionRequest transactionRequest);
    List<UserTransactionsHistoryResponse> getUserTransactionsHistory();
    List<CampaignTransactionsHistoryResponse> getCampaignTransactionsHistory(Campaign campaign);
    Page<TransactionResponse> getAllTransaction(int year, Pageable pageable);

    double getTotalTransactionCount();
    double getTotalDonationCampaign();

    Map<String, Double> getUserTransactionSummary();
    Map<String, Double> getUserTransactionSummaryByYear(int year);

}
