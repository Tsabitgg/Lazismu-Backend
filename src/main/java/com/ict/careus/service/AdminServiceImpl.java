package com.ict.careus.service;

import com.ict.careus.model.User;
import com.ict.careus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
