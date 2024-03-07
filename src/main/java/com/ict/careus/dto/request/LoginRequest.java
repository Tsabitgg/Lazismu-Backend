package com.ict.careus.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String noPhone;
    private String password;
}