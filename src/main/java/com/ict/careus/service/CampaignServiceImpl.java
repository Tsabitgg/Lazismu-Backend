package com.ict.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ict.careus.dto.request.CampaignRequest;
import com.ict.careus.enumeration.CampaignCategory;
import com.ict.careus.model.Campaign;
import com.ict.careus.model.Category;
import com.ict.careus.repository.CampaignRepository;
import com.ict.careus.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CampaignServiceImpl implements CampaignService{

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Campaign createCampaign(CampaignRequest campaignRequest) {
        Category category = categoryRepository.findById(campaignRequest.getCategoryId()).get();

        // Membuat objek Campaign baru dengan menggunakan model mapper
        Campaign campaign = modelMapper.map(campaignRequest, Campaign.class);
        campaign.setCategory(category);

        if (campaignRequest.getCampaignImage() != null && !campaignRequest.getCampaignImage().isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        campaignRequest.getCampaignImage().getBytes(),
                        ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();
                campaign.setCampaignImage(imageUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String baseurl = "www.careus.com/campaign/";
        campaign.setGeneratelink(baseurl + campaign.getCampaignCode());

        // Menyimpan objek Campaign baru ke dalam basis data
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
