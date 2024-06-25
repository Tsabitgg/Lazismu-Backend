package com.ict.careus.controller;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.model.transaction.Billing;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.service.BillingService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping("/billing/{transactionType}/{code}")
    public ResponseEntity<?> createTransaction(@PathVariable String transactionType,
                                               @PathVariable String code,
                                               @RequestBody TransactionRequest transactionRequest) {
        try {
            Billing billing = billingService.createBilling(transactionType, code, transactionRequest);
            return ResponseEntity.ok(billing);
        } catch (RuntimeException | BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/billing/success/{billingId}")
    public Map<String, String> getBillingSuccess(@PathVariable Long billingId) {
        boolean success = billingService.getBillingSuccess(billingId);
        Map<String, String> response = new HashMap<>();
        response.put("success", String.valueOf(success));
        return response;
    }
}
