package com.ict.careus.service;

import com.ict.careus.dto.response.SummaryResponse;
import com.ict.careus.repository.DistributionRepository;
import com.ict.careus.repository.TransactionRepository;
import com.ict.careus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private DistributionRepository distributionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public SummaryResponse getSummary(Integer year) {
        SummaryResponse summary = new SummaryResponse();
        if (year != null) {
            summary.setTotalDistributionAmount(distributionRepository.totalDistributionAmountByYear(year));
            summary.setTotalDistributionReceiver(distributionRepository.totalDistributionReceiverByYear(year));
            summary.setTotalTransactionAmount(transactionRepository.getTotalTransactionAmountByYear(year));
            summary.setTotalUser(userRepository.getTotalUserByYear(year));
        } else {
            summary.setTotalDistributionAmount(distributionRepository.totalDistributionAmount());
            summary.setTotalDistributionReceiver(distributionRepository.totalDistributionReceiver());
            summary.setTotalTransactionAmount(transactionRepository.totalTransactionAmount());
            summary.setTotalUser(userRepository.getTotalUser());
        }
        return summary;
    }
}
