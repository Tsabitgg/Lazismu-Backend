package com.ict.careus.controller;

import com.ict.careus.dto.request.EditProfileRequest;
import com.ict.careus.dto.response.UserTransactionsHistoryResponse;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.model.user.User;
import com.ict.careus.service.TransactionService;
import com.ict.careus.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/admin/get-all-user")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> user = userService.getAllUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/{id}/history")
    public ResponseEntity<List<UserTransactionsHistoryResponse>> getUserTransactionsHistory(@PathVariable long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        List<UserTransactionsHistoryResponse> userTransactionsDTO = transactionService.getUserTransactionsHistory(user);
        return ResponseEntity.ok(userTransactionsDTO);
    }

    @GetMapping("/user/summary")
    public ResponseEntity<Map<String, Double>> getUserTransactionSummary(@RequestParam("userId") Long userId,
                                                                         @RequestParam(name = "year", required = false) Integer year) {
        if (year == null) {
            Map<String, Double> summary = transactionService.getUserTransactionSummary(userId);
            return ResponseEntity.ok().body(summary);
        } else {
            Map<String, Double> summary = transactionService.getUserTransactionSummaryByYear(userId, year);
            return ResponseEntity.ok().body(summary);
        }
    }

    @PutMapping("user/edit-profile")
    public ResponseEntity<User> editProfile(@ModelAttribute EditProfileRequest editProfileRequest) throws BadRequestException {
        return ResponseEntity.ok().body(userService.editProfile(editProfileRequest));
    }
}
