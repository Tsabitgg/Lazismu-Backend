package com.ict.careus.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private int id;
    private String username;
    private String phoneNumber;
    private List<String> roles;

    public JwtResponse(String accessToken, int id, String username, String phoneNumber, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }
}