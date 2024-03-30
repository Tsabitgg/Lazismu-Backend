package com.ict.careus.service;

import com.ict.careus.dto.request.CampaignRequest;
import com.ict.careus.model.campaign.Campaign;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CampaignService {
    Campaign createCampaign(CampaignRequest campaignRequest) throws BadRequestException;
    Campaign updateCampaign(String campaignCode, CampaignRequest campaignRequest) throws BadRequestException;
    void deleteCampaign(long campaignId) throws BadRequestException;
    Page<Campaign> getAllCampaign(Pageable pageable);
    List<Campaign> getCampaignActive(boolean isActive);
    Optional<Campaign> getCampaignByCode(String campaignCode);
    Optional<Campaign> getCampaignById(long campaignId);
    Page<Campaign> getCampaignByName(String campaignName, Pageable pageable);
    Page<Campaign> getCampaignByCategoryName(String categoryName, Pageable pageable);
    Page<Campaign> getCampaignsByYear(int year, Pageable pageable);
}
