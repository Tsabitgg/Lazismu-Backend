package com.ict.careus.controller;

import com.ict.careus.dto.request.DistributionRequest;
import com.ict.careus.model.transaction.Distribution;
import com.ict.careus.service.DistributionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class DistributionController {

    @Autowired
    private DistributionService distributionService;

    @PostMapping("/distribution/{type}/{code}")
    public ResponseEntity<Distribution> createDistributions(@PathVariable("type") String type,
                                                            @PathVariable("code") String code,
                                                            @ModelAttribute DistributionRequest distributionRequest) throws BadRequestException {
        Distribution distributions = distributionService.createDistribution(type, code, distributionRequest);
        return ResponseEntity.ok().body(distributions);
    }
}
