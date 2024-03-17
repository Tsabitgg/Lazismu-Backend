package com.ict.careus.controller;


import com.ict.careus.dto.request.CampaignRequest;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping("/admin/create-campaign")
    public ResponseEntity<Campaign> createCampaign(@ModelAttribute CampaignRequest campaignRequest){
        Campaign createdCampaign = campaignService.createCampaign(campaignRequest);

        return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
    }

    @PutMapping("/admin/update-campaign/{campaignCode}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable String campaignCode, @ModelAttribute CampaignRequest campaignRequest){
        Campaign updatedCampaign = campaignService.updateCampaign(campaignCode, campaignRequest);
        if (updatedCampaign != null){
            return ResponseEntity.ok(updatedCampaign);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/campaign")
    public ResponseEntity<List<Campaign>>getAllCampaign(){
        List<Campaign> campaign = campaignService.getAllCampaign();
        return ResponseEntity.ok(campaign);
    }

    @GetMapping("/campaign/active")
    public ResponseEntity<List<Campaign>> getCampaignActive(){
        List<Campaign> activeCampaign = campaignService.getCampaignActive(true);
        return ResponseEntity.ok(activeCampaign);
    }


    @GetMapping("/campaign/{campaignCode}")
    public ResponseEntity<Campaign> getCampaignByCode(@PathVariable String campaignCode) {
        Optional<Campaign> campaignOptional = campaignService.getCampaignByCode(campaignCode);
        if (campaignOptional.isPresent()) {
            return new ResponseEntity<>(campaignOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/campaign/category/{categoryName}")
    public List<Campaign> getCampaigByCategoryName(@PathVariable String categoryName) {
        return campaignService.getCampaignByCategoryName(categoryName);
    }
}
