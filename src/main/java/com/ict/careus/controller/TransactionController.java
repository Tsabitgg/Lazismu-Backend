package com.ict.careus.controller;

import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{transactionType}/{code}")
    public ResponseEntity<Transaction> createTransaction(@PathVariable("transactionType") String transactionType,
                                                         @PathVariable("code") String code,
                                                         @RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transactionType, code, transaction);
        return ResponseEntity.ok().body(createdTransaction);
    }

}
