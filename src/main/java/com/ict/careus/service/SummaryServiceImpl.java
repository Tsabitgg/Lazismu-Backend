package com.ict.careus.service;

import com.ict.careus.dto.response.*;
import com.ict.careus.enumeration.InfakCategory;
import com.ict.careus.enumeration.WakafCategory;
import com.ict.careus.enumeration.ZakatCategory;
import com.ict.careus.model.ziswaf.Zakat;
import com.ict.careus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private DistributionRepository distributionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private WakafRepository wakafRepository;

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

    @Override
    public List<AmilCampaignResponse> getAmilCampaign() {
        List<Object[]> results = campaignRepository.getAmilCampaign();
        return results.stream().map(result ->
                new AmilCampaignResponse(
                        (Long) result[0],
                        (String) result[1],
                        (String) result[2],
                        (Double) result[3],
                        (Double) result[4],
                        (Double) result[5],
                        (Boolean) result[6]
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<AmilZakatResponse> getAmilZakat() {
        List<Object[]> results = zakatRepository.getAmilZakat();
        return results.stream().map(result ->
                new AmilZakatResponse(
                        (Long) result[0],
                        (ZakatCategory) result[1],
                        (String) result[2],
                        (Double) result[3],
                        (Double) result[4]
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<AmilInfakResponse> getAmilInfak() {
        List<Object[]> results = infakRepository.getAmilInfak();
        return results.stream().map(result ->
                new AmilInfakResponse(
                        (Long) result[0],
                        (InfakCategory) result[1],
                        (String) result[2],
                        (Double) result[3],
                        (Double) result[4]
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<AmilWakafResponse> getAmilWakaf() {
        List<Object[]> results = wakafRepository.getAmilWakaf();
        return results.stream().map(result ->
                new AmilWakafResponse(
                        (Long) result[0],
                        (WakafCategory) result[1],
                        (String) result[2],
                        (Double) result[3],
                        (Double) result[4]
                )
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<SummaryCampaignResponse> getSummaryCampaign() {
        Optional<Map<String, Double>> summaryMap = campaignRepository.getSummaryCampaign();
        return summaryMap.map(map -> new SummaryCampaignResponse(
                map.getOrDefault("totalCampaignTransactionAmount", 0.0),
                map.getOrDefault("totalAmil", 0.0),
                map.getOrDefault("totalCampaignDistributionAmount", 0.0)));
    }
}
