package com.ecomhub.user.service.controller.authentication;

import com.ecomhub.user.service.service.authentication.AccountService;
import com.ecomhub.user.service.service.authentication.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/otp")
public class OTPController {

    @Autowired
    public OtpService otpService;

    @Autowired
    public AccountService accountService;


    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        log.info("OTP verification request received for email: {}", email);

        accountService.verifyAccount(email, otp);

        return ResponseEntity.status(HttpStatus.OK).body("OTP verified successfully");
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendOtp(@RequestParam("email") String email) {
        log.info("resend otp request received: {}", email);

        otpService.sendOtp(email);

        return ResponseEntity.status(HttpStatus.OK).body("OTP resent successfully");
    }

}
