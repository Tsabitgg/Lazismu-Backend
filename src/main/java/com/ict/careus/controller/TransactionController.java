package com.ict.careus.controller;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.dto.response.TransactionResponse;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.service.TransactionService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.Year;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction/{transactionType}/{code}")
    public ResponseEntity<?> createTransaction(@PathVariable String transactionType,
                                               @PathVariable String code,
                                               @RequestBody TransactionRequest transactionRequest) {
        try {
            Transaction transaction = transactionService.createTransaction(transactionType, code, transactionRequest);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException | BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @GetMapping("/qr/{transactionId}")
//    public ResponseEntity<?> getQRCode(@PathVariable long transactionId) {
//        try {
//            byte[] qrCode = transactionService.generateQRCode(transactionId);
//            // Mengubah byte array menjadi InputStream
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(qrCode);
//            // Membaca sebagai gambar PNG
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_PNG)
//                    .body(inputStream.readAllBytes());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }


    @GetMapping("/total-transaction-count")
    public double getTotalTransactionCount(){
        return transactionService.getTotalTransactionCount();
    }

    @GetMapping("/admin/get-all-transactions")
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
}

