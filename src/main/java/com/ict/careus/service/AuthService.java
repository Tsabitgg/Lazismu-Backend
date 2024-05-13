package com.ict.careus.service;

import com.ict.careus.dto.request.LoginRequest;
import com.ict.careus.dto.request.SignupRequest;
import com.ict.careus.dto.response.JwtResponse;
import com.ict.careus.model.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) throws BadRequestException;
    User registerUser(SignupRequest signUpRequest) throws BadRequestException;
}
