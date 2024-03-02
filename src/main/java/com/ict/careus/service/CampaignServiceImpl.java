package com.ict.careus.service;

import com.ict.careus.enumeration.CampaignCategory;
import com.ict.careus.model.Campaign;
import com.ict.careus.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignServiceImpl implements CampaignService{

    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public Campaign createCampaign(Campaign campaign) {
        String baseurl = "www.careus.com/campaign/";
        campaign.setGeneratelink(baseurl+campaign.getCampaignCode());

        return campaignRepository.save(campaign);
    }

    @Override
    public Campaign updateCampaign(String campaignCode, Campaign campaign) {
        Campaign updateCampaign = campaignRepository.findByCampaignCode(campaignCode);
        if (updateCampaign != null){
            updateCampaign.setCampaignCode(campaign.getCampaignCode());
            updateCampaign.setCampaignName(campaign.getCampaignName());
            updateCampaign.setCampaignImage(campaign.getCampaignImage());
            updateCampaign.setDescription(campaign.getDescription());
            updateCampaign.setLocation(campaign.getLocation());
            updateCampaign.setVaNumber(campaign.getVaNumber());
            updateCampaign.setTargetAmount(campaign.getTargetAmount());
            updateCampaign.setCurrentAmount(campaign.getCurrentAmount());
            updateCampaign.setActive(campaign.isActive());
            return campaignRepository.save(updateCampaign);
        }
        return null;
    }

    @Override
    public List<Campaign> getAllCampaign() {
        return campaignRepository.findAll();
    }

    @Override
    public List<Campaign> getCampaignActive(boolean isActive) {
        return campaignRepository.findCampaignByActive(isActive);
    }

    @Override
    public Optional<Campaign> getCampaignByCode(String campaignCode) {
        return Optional.ofNullable(campaignRepository.findByCampaignCode(campaignCode));
    }

    @Override
    public List<Campaign> getCampaignByCategoryName(String categoryName) {
        return campaignRepository.findByCategoryName(CampaignCategory.valueOf(categoryName.toUpperCase()));
    }

}
