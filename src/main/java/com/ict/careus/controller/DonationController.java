package com.ict.careus.controller;

import com.ict.careus.model.Donation;
import com.ict.careus.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @PostMapping("/donation/{campaignCode}")
    public ResponseEntity<Donation> createDonation(@PathVariable String campaignCode,
                                                   @RequestBody Donation donation) {
        Donation createdDonation = donationService.createDonation(campaignCode, donation);
        return ResponseEntity.ok(createdDonation);
    }
}
