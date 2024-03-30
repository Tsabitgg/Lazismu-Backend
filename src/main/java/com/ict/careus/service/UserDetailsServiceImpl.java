package com.ict.careus.service;

import com.ict.careus.model.user.User;
import com.ict.careus.repository.UserRepository;
import com.ict.careus.service.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user != null){
            return UserDetailsImpl.build(user);
        }
        throw new UsernameNotFoundException("User not found with phoneNumber " + phoneNumber);
    }
}