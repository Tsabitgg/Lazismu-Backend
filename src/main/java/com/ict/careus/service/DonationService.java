package com.ict.careus.service;

import com.ict.careus.model.Donation;

public interface DonationService {
    Donation createDonation(String campaignCode, Donation donation);
}
