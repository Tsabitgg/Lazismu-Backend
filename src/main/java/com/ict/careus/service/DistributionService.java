package com.ict.careus.service;

import com.ict.careus.dto.request.DistributionRequest;
import com.ict.careus.dto.response.CampaignDistributionHistoryResponse;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Distribution;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DistributionService {
    Distribution createDistribution(String type, String code, DistributionRequest distributionRequest) throws BadRequestException;
    Page<CampaignDistributionHistoryResponse> getCampaignDistributionHistory(Campaign campaign, Pageable pageable);
    Page<Distribution> getAllDistribution(Pageable pageable);
}
