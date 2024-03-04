package com.ict.careus.service;

import com.ict.careus.model.Campaign;
import com.ict.careus.model.Donation;
import com.ict.careus.model.User;
import com.ict.careus.repository.CampaignRepository;
import com.ict.careus.repository.DonationRepository;
import com.ict.careus.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class DonationServiceImpl implements DonationService{

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Donation createDonation(String campaignCode, Donation donation) {
        Campaign campaign = campaignRepository.findByCampaignCode(campaignCode);
        if (campaign != null) {
            donation.setCampaign(campaign);
            donation.setDonationDate(new Date());
            donation.setSuccess(true);

            donation = donationRepository.save(donation);

            donationRepository.update_campaign_current_amount(campaignCode, donation.getAmount());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String password = dateFormat.format(new Date());

            User user = userRepository.findByNoPhone(donation.getNoPhone());
            if (user == null){
                user = new User();
                user.setName(donation.getName());
                user.setNoPhone(donation.getNoPhone());
                user.setPassword(password);
                user.setActive(true);
                userRepository.save(user);
            }
            return donation;
        } else {
            throw new RuntimeException("Campaign not found with code: " + campaignCode);
        }
    }
}
