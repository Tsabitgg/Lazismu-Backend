package com.ict.careus.controller;

import com.ict.careus.dto.request.DistributionRequest;
import com.ict.careus.model.transaction.Distribution;
import com.ict.careus.service.DistributionService;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

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

    @GetMapping("/admin/get-all-distributions")
    public ResponseEntity<Page<Distribution>> getAllDistribution(@RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Distribution> distributions = distributionService.getAllDistribution(pageRequest);

        return ResponseEntity.ok(distributions);
    }

}
