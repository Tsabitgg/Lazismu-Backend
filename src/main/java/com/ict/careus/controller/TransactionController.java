package com.ict.careus.controller;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.dto.response.TransactionResponse;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions/{transactionType}/{code}")
    public ResponseEntity<TransactionResponse> createTransaction(@PathVariable("transactionType") String transactionType,
                                                                 @PathVariable("code") String code,
                                                                 @RequestBody TransactionRequest transactionRequest) {
        Transaction createdTransaction = transactionService.createTransaction(transactionType, code, transactionRequest);

        TransactionResponse transactionResponse = new TransactionResponse(createdTransaction);

        return ResponseEntity.ok().body(transactionResponse);
    }

    @GetMapping("admin/get-all-transactions")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(@RequestParam(name = "year", required = false) Integer year,
                                                @RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        if (year == null) {
            year = Year.now().getValue();
        }

        Page<TransactionResponse> transactions = transactionService.getAllTransaction(year, pageRequest);

        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/campaign/total-donation")
    public double getTotalDonationCampaign(){
        return transactionService.getTotalDonationCampaign();
    }

    @GetMapping("/total-transaction-count")
    public double getTotalTransactionCount(){
        return transactionService.getTotalTransactionCount();
    }
}

