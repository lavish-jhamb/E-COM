package com.ecomhub.user.service.controller;

import com.ecomhub.user.service.dto.request.RegisterRequest;
import com.ecomhub.user.service.dto.request.LoginRequest;
import com.ecomhub.user.service.dto.response.LoginResponse;
import com.ecomhub.user.service.dto.response.RegisterResponse;
import com.ecomhub.user.service.response.ApiResponse;
import com.ecomhub.user.service.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequest request) {
        log.info("user registration request received: {}", request);

        RegisterResponse registerResponse = authenticationService.register(request);

        log.info("user registered successfully: {}", registerResponse);

        ApiResponse<RegisterResponse> response = new ApiResponse<>(
                true,
                "User registered successfully",
                registerResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {
        log.info("user login request received: {}", request);

        LoginResponse loginResponse = authenticationService.login(request);

        ApiResponse<LoginResponse> response = new ApiResponse<>(
                true,
                "User logged in successfully",
                loginResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
