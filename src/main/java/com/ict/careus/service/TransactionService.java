package com.ict.careus.service;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.dto.response.CampaignTransactionsHistoryResponse;
import com.ict.careus.dto.response.TransactionResponse;
import com.ict.careus.dto.response.UserTransactionsHistoryResponse;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Transaction;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionService {

    Transaction createTransaction(String transactionType, String code, TransactionRequest transactionRequest) throws BadRequestException;
    List<UserTransactionsHistoryResponse> getUserTransactionsHistory() throws BadRequestException;

//    String handleInquiry(String token);
//
//    String handlePayment(String token);
//
//    String handleReversal(String token);

    Page<CampaignTransactionsHistoryResponse> getCampaignTransactionsHistory(Campaign campaign, Pageable pageable);
    Page<Transaction> getAllTransaction(int year, Pageable pageable);

    double getTotalTransactionCount();
    double getTotalDonationCampaign();

    Map<String, Double> getUserTransactionSummary() throws BadRequestException;
    Map<String, Double> getUserTransactionSummaryByYear(int year) throws BadRequestException;

    Optional<Transaction> getTransactionById(long transactionId);

    Page<Transaction> getZakatTransaction(Pageable pageable);
    Page<Transaction> getInfakTransaction(Pageable pageable);
    Page<Transaction> getWakafTransaction(Pageable pageable);
}
