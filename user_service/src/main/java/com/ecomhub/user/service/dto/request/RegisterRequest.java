package com.ecomhub.user.service.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private boolean seller;
}
