package com.ict.careus.service;

import com.ict.careus.dto.request.CampaignRequest;
import com.ict.careus.enumeration.CampaignCategory;
import com.ict.careus.model.Campaign;
import com.ict.careus.model.Category;

import java.util.List;
import java.util.Optional;

public interface CampaignService {
    Campaign createCampaign(CampaignRequest campaignRequest);

    Campaign updateCampaign(String campaignCode, Campaign campaign);
    List<Campaign> getAllCampaign();
    List<Campaign> getCampaignActive(boolean isActive);
    Optional<Campaign> getCampaignByCode(String campaignCode);
    List<Campaign> getCampaignByCategoryName(String categoryName);
}
