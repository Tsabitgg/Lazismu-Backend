package com.ict.careus.controller;

import com.ict.careus.model.User;
import com.ict.careus.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {


    @Autowired
    private AdminService adminService;

    @GetMapping(value = "/admin/get-all-user")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> user = adminService.getAllUser();
        return ResponseEntity.ok(user);
    }
}
