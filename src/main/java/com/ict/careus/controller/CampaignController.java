package com.ict.careus.controller;

import com.ict.careus.dto.request.CampaignRequest;
import com.ict.careus.dto.response.CampaignTransactionsHistoryResponse;;
import com.ict.careus.dto.response.MessageResponse;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.service.CampaignService;
import com.ict.careus.service.TransactionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/admin/create-campaign")
    public ResponseEntity<?> createCampaign(@ModelAttribute CampaignRequest campaignRequest) {
        try {
            Campaign createdCampaign = campaignService.createCampaign(campaignRequest);
            return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/admin/update-campaign/{campaignCode}")
    public ResponseEntity<?> updateCampaign(@PathVariable String campaignCode, @ModelAttribute CampaignRequest campaignRequest) {
        try{
            Campaign updatedCampaign = campaignService.updateCampaign(campaignCode, campaignRequest);
            return new ResponseEntity<>(updatedCampaign, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/delete-campaign/{campaignId}")
    public MessageResponse deleteCampaign(@PathVariable long campaignId) throws BadRequestException {
        campaignService.deleteCampaign(campaignId);
        return new MessageResponse("Delete campaign successfully");
    }

    @GetMapping("/campaign")
    public Page<Campaign> getCampaigns(@RequestParam(name = "year", required = false) Integer year,
                                       @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12; // Jumlah campaign per halaman
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        if (year != null) {
            return campaignService.getCampaignsByYear(year, pageRequest);
        } else {
            return campaignService.getAllCampaign(pageRequest);
        }
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

    @GetMapping("/campaign/id")
    public ResponseEntity<Campaign> getCampaignById(@RequestParam long campaignId) {
        Optional<Campaign> campaignOptional = campaignService.getCampaignById(campaignId);
        if (campaignOptional.isPresent()){
            return new ResponseEntity<>(campaignOptional.get(), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/campaign/search")
    public Page<Campaign> getCampaignByName(@RequestParam String campaignName,
                                            @RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12; // Jumlah campaign per halaman
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        return campaignService.getCampaignByName(campaignName, pageRequest);
    }

    @GetMapping("/campaign/category")
    public Page<Campaign> getCampaigByCategoryName(@RequestParam String categoryName,
                                                   @RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12; // Jumlah campaign per halaman
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return campaignService.getCampaignByCategoryName(categoryName, pageRequest);
    }

    @GetMapping("/campaign/{campaignCode}/history")
    public ResponseEntity<List<CampaignTransactionsHistoryResponse>> getCampaignTransactionsHistory(@PathVariable String campaignCode) {
        Optional<Campaign> campaignOptional = campaignService.getCampaignByCode(campaignCode);
        if (!campaignOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Campaign campaign = campaignOptional.get();
        List<CampaignTransactionsHistoryResponse> campaignTransactionsDTO = transactionService.getCampaignTransactionsHistory(campaign);
        return ResponseEntity.ok(campaignTransactionsDTO);
    }

    @GetMapping("/campaign/total-donation")
    public double getTotalDonationCampaign(){
        return transactionService.getTotalDonationCampaign();
    }
}
