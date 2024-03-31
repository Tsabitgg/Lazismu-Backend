package com.ict.careus.controller;

import com.ict.careus.dto.request.EditProfileRequest;
import com.ict.careus.dto.response.MessageResponse;
import com.ict.careus.dto.response.UserTransactionsHistoryResponse;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.model.user.User;
import com.ict.careus.service.TransactionService;
import com.ict.careus.service.UserDetailsImpl;
import com.ict.careus.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = {"Authorization", "Content-Type", "Cookie"}, allowCredentials = "true")
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

    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);

        if (currentUser != null) {
            return ResponseEntity.ok().body(currentUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
    }

    @GetMapping("/user/history")
    public ResponseEntity<List<UserTransactionsHistoryResponse>> getUserTransactionsHistory() {
        List<UserTransactionsHistoryResponse> userTransactionsDTO = transactionService.getUserTransactionsHistory();
        return ResponseEntity.ok(userTransactionsDTO);
    }

    @GetMapping("/user/summary")
    public ResponseEntity<Map<String, Double>> getUserTransactionSummary(@RequestParam(name = "year", required = false) Integer year) {
        if (year == null) {
            Map<String, Double> summary = transactionService.getUserTransactionSummary();
            return ResponseEntity.ok().body(summary);
        } else {
            Map<String, Double> summary = transactionService.getUserTransactionSummaryByYear(year);
            return ResponseEntity.ok().body(summary);
        }
    }

    @PutMapping("/user/edit-profile")
    public MessageResponse editProfile(@ModelAttribute EditProfileRequest editProfileRequest) throws BadRequestException {
        return new MessageResponse("profile updated successfully");
    }
}
