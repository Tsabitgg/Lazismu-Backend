package com.ict.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ict.careus.dto.request.DistributionRequest;
import com.ict.careus.enumeration.ERole;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Distribution;
import com.ict.careus.model.user.User;
import com.ict.careus.model.ziswaf.Infak;
import com.ict.careus.model.ziswaf.Wakaf;
import com.ict.careus.model.ziswaf.Zakat;
import com.ict.careus.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @Autowired
    private UserRepository userRepository;

    @Override
    public Distribution createDistribution(String distributionType, String code, DistributionRequest distributionRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can Distribute");
            }

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
        throw new BadRequestException("Admin not found");
    }
}