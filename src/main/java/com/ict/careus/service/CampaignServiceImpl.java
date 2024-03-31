package com.ict.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ict.careus.dto.request.CampaignRequest;
import com.ict.careus.enumeration.CampaignCategory;
import com.ict.careus.enumeration.ERole;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.campaign.Category;
import com.ict.careus.model.user.User;
import com.ict.careus.repository.CampaignRepository;
import com.ict.careus.repository.CategoryRepository;
import com.ict.careus.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Campaign createCampaign(CampaignRequest campaignRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            Category category = categoryRepository.findById(campaignRequest.getCategoryId()).orElseThrow(() -> new BadRequestException("Category not found"));
            Campaign campaign = modelMapper.map(campaignRequest, Campaign.class);
            campaign.setCategory(category);

            if (campaignRepository.findByCampaignCode(campaignRequest.getCampaignCode()) != null) {
                throw new BadRequestException("Error: campaignCode is already taken!");
            }

            if (campaignRequest.getCampaignImage() != null && !campaignRequest.getCampaignImage().isEmpty()) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(
                            campaignRequest.getCampaignImage().getBytes(),
                            ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("url").toString();
                    campaign.setCampaignImage(imageUrl);

                } catch (IOException e) {
                    throw new BadRequestException("Error uploading image", e);
                }
            }

            String baseurl = "www.careus.com/campaign/";
            campaign.setGenerateLink(baseurl + campaign.getCampaignCode());

            return campaignRepository.save(campaign);
        }
        throw new BadRequestException("Admin not found");
    }


    @Override
    @Transactional
    public Campaign updateCampaign(String campaignCode, CampaignRequest campaignRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            // Pastikan pengguna memiliki peran "ADMIN"
            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can update campaigns");
            }

            Campaign updateCampaign = campaignRepository.findByCampaignCode(campaignCode);

            if (updateCampaign != null) {
                if (!campaignCode.equals(campaignRequest.getCampaignCode()) &&
                        campaignRepository.findByCampaignCode(campaignRequest.getCampaignCode()) != null) {
                    throw new BadRequestException("campaignCode is already taken!");
                }

                updateCampaign.setCampaignCode(campaignRequest.getCampaignCode());
                updateCampaign.setCampaignName(campaignRequest.getCampaignName());
                updateCampaign.setDescription(campaignRequest.getDescription());
                updateCampaign.setLocation(campaignRequest.getLocation());
                updateCampaign.setVaNumber(campaignRequest.getVaNumber());
                updateCampaign.setTargetAmount(campaignRequest.getTargetAmount());
                updateCampaign.setCurrentAmount(campaignRequest.getCurrentAmount());
                updateCampaign.setCreator(campaignRequest.getCreator());
                updateCampaign.setActive(campaignRequest.isActive());

                if (campaignRequest.getCampaignImage() != null && !campaignRequest.getCampaignImage().isEmpty()) {
                    try {
                        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                                campaignRequest.getCampaignImage().getBytes(),
                                ObjectUtils.emptyMap());
                        String imageUrl = uploadResult.get("url").toString();
                        updateCampaign.setCampaignImage(imageUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String baseUrl = "www.careus.com/campaign/";
                updateCampaign.setGenerateLink(baseUrl + updateCampaign.getCampaignCode());

                return campaignRepository.save(updateCampaign);
            } else {
                throw new BadRequestException("Campaign not found!");
            }
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    @Transactional
    public void deleteCampaign(long campaignId) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (!existingUser.getRole().getName().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can delete campaigns");
            }
            campaignRepository.deleteById(campaignId);
        }
    }

    @Override
    public Page<Campaign> getAllCampaign(Pageable pageable) {
        return campaignRepository.findAll(pageable);
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
    public Optional<Campaign> getCampaignById(long campaignId) {
        return Optional.ofNullable(campaignRepository.findById(campaignId));
    }

    @Override
    public Page<Campaign> getCampaignByName(String campaignName, Pageable pageable) {
        return campaignRepository.findByCampaignName(campaignName, pageable);
    }

    @Override
    public Page<Campaign> getCampaignByCategoryName(String categoryName, Pageable pageable) {
        return campaignRepository.findByCategoryName(CampaignCategory.valueOf(categoryName.toUpperCase()), pageable);
    }

    @Override
    public Page<Campaign> getCampaignsByYear(int year, Pageable pageable) {
        return campaignRepository.findByYear(year, pageable);
    }
}
