package com.ict.careus.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private int id;
    private String username;
    private String noPhone;
    private List<String> roles;

    public JwtResponse(String accessToken, int id, String username, String noPhone, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.noPhone = noPhone;
        this.roles = roles;
    }
}