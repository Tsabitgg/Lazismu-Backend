package com.ict.careus.controller;

import com.ict.careus.dto.response.*;
import com.ict.careus.model.ziswaf.Zakat;
import com.ict.careus.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary(@RequestParam(name = "year", required = false) Integer year) {
        SummaryResponse summary = summaryService.getSummary(year);
        return ResponseEntity.ok().body(summary);
    }

    @GetMapping("/amil-campaign")
    public List<AmilCampaignResponse> getAmilCampaign(){
        return summaryService.getAmilCampaign();
    }

    @GetMapping("/amil-zakat")
    public List<AmilZakatResponse> getAmilZakat(){
        return summaryService.getAmilZakat();
    }

    @GetMapping("/amil-infak")
    public List<AmilInfakResponse> getAmilInfak(){
        return summaryService.getAmilInfak();
    }

    @GetMapping("/amil-wakaf")
    public List<AmilWakafResponse> getAmilWakaf(){
        return summaryService.getAmilWakaf();
    }

    @GetMapping("summary-campaign")
    public ResponseEntity<SummaryCampaignResponse> getSummaryCampaign() {
        Optional<SummaryCampaignResponse> summary = summaryService.getSummaryCampaign();
        return summary.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}