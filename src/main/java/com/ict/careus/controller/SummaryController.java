package com.ict.careus.controller;

import com.ict.careus.dto.response.SummaryResponse;
import com.ict.careus.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary(@RequestParam(name = "year", required = false) Integer year) {
        SummaryResponse summary = summaryService.getSummary(year);
        return ResponseEntity.ok().body(summary);
    }
}