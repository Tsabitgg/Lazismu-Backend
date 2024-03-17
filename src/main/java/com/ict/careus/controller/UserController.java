package com.ict.careus.controller;

import com.ict.careus.dto.response.UserTransactionsHistoryResponse;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.model.user.User;
import com.ict.careus.service.TransactionService;
import com.ict.careus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/users/{id}/history")
    public ResponseEntity<List<UserTransactionsHistoryResponse>> getUserTransactionsHistory(@PathVariable long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        List<UserTransactionsHistoryResponse> userTransactionsDTO = transactionService.getUserTransactionsHistory(user);
        return ResponseEntity.ok(userTransactionsDTO);
    }


}
