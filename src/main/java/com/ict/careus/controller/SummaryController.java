package com.ict.careus.controller;

import com.ict.careus.dto.response.SummaryResponse;
import com.ict.careus.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping
    public ResponseEntity<SummaryResponse> getSummary(@RequestParam(name = "year", required = false) Integer year) {
        SummaryResponse summary = summaryService.getSummary(year);
        return ResponseEntity.ok().body(summary);
    }
}