package com.ecomhub.user.service.controller.authentication;

import com.ecomhub.user.service.common.constants.ApiPaths;
import com.ecomhub.user.service.dto.request.RegisterRequest;
import com.ecomhub.user.service.dto.request.LoginRequest;
import com.ecomhub.user.service.dto.response.LoginResponse;
import com.ecomhub.user.service.dto.response.RegisterResponse;
import com.ecomhub.user.service.response.ApiResponse;
import com.ecomhub.user.service.service.authentication.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        log.info("account registration request received - email: {}, seller: {}", request.getEmail(), request.isSeller());

        RegisterResponse registerResponse = accountService.register(request);

        ApiResponse<RegisterResponse> response = new ApiResponse<>(
                true,
                "account registered successfully",
                registerResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        log.info("account login request received: {}", request.getEmail());

        LoginResponse loginResponse = accountService.login(request);

        ApiResponse<LoginResponse> response = new ApiResponse<>(
                true,
                "account logged in successfully",
                loginResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
