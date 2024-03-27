package com.ict.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ict.careus.dto.request.DistributionRequest;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Distribution;
import com.ict.careus.model.ziswaf.Infak;
import com.ict.careus.model.ziswaf.Wakaf;
import com.ict.careus.model.ziswaf.Zakat;
import com.ict.careus.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class DistributionServiceImpl implements DistributionService {

    @Autowired
    private DistributionRepository distributionRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Distribution createDistribution(String distributionType, String code, DistributionRequest distributionRequest) {
        Distribution distribution = modelMapper.map(distributionRequest, Distribution.class);

        switch (distributionType) {
            case "campaign":
                Campaign campaign = campaignRepository.findByCampaignCode(code);
                if (campaign != null) {
                    distribution.setCampaign(campaign);
                    distributionRepository.update_campaign_distribution(code, distribution.getDistributionAmount());
                } else {
                    throw new RuntimeException("Campaign not found with code: " + code);
                }
                break;
            case "zakat":
                Zakat zakat = zakatRepository.findByZakatCode(code);
                if (zakat != null) {
                    distribution.setZakat(zakat);
                    distributionRepository.update_zakat_distribution(code, distribution.getDistributionAmount());
                } else {
                    throw new RuntimeException("Zakat not found with code: " + code);
                }
                break;
            case "infak":
                Infak infak = infakRepository.findByInfakCode(code);
                if (infak != null) {
                    distribution.setInfak(infak);
                    distributionRepository.update_infak_distribution(code, distribution.getDistributionAmount());
                } else {
                    throw new RuntimeException("Infak not found with code: " + code);
                }
                break;
            case "wakaf":
                Wakaf wakaf = wakafRepository.findByWakafCode(code);
                if (wakaf != null) {
                    distribution.setWakaf(wakaf);
                    distributionRepository.update_wakaf_distribution(code, distribution.getDistributionAmount());
                } else {
                    throw new RuntimeException("Wakaf not found with code: " + code);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid distribution distributionType: " + distributionType);
        }

        if (distributionRequest.getImage() != null && !distributionRequest.getImage().isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        distributionRequest.getImage().getBytes(),
                        ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();
                distribution.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image.", e);
            }
        }
        distribution.setCategory(distributionType);
        return distributionRepository.save(distribution);
    }
}