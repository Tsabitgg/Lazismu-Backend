package com.ict.careus.service;

import com.ict.careus.model.User;

import java.util.List;

public interface AdminService {

    List<User> getAllUser();

    //Admin findByUsername(String username);
    //Admin saveAdmin(Admin admin);
}
