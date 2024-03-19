package com.ict.careus.service;

import com.cloudinary.api.exceptions.BadRequest;
import com.ict.careus.dto.request.CampaignRequest;
import com.ict.careus.model.campaign.Campaign;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface CampaignService {
    Campaign createCampaign(CampaignRequest campaignRequest) throws BadRequestException;
    Campaign updateCampaign(String campaignCode, CampaignRequest campaignRequest) throws BadRequestException;
    List<Campaign> getAllCampaign();
    List<Campaign> getCampaignActive(boolean isActive);
    Optional<Campaign> getCampaignByCode(String campaignCode);
    Optional<Campaign> getCampaignById(long campaignId);
    List<Campaign> getCampaignByName(String campaignName);
    List<Campaign> getCampaignByCategoryName(String categoryName);
}
