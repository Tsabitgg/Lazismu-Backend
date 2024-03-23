package com.ict.careus.service;

import com.ict.careus.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUser();

    Optional<User> findById(Long id);

    long getTotalUser();
}
