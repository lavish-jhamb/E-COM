package com.ecomhub.user.service.service;

import com.ecomhub.user.service.dto.request.RegisterRequest;
import com.ecomhub.user.service.dto.request.LoginRequest;
import com.ecomhub.user.service.dto.response.LoginResponse;
import com.ecomhub.user.service.dto.response.RegisterResponse;
import com.ecomhub.user.service.entity.Account;
import com.ecomhub.user.service.exception.InvalidCredentialsException;
import com.ecomhub.user.service.exception.AccountAlreadyExistException;
import com.ecomhub.user.service.exception.AccountNotFoundException;
import com.ecomhub.user.service.entity.Profile;
import com.ecomhub.user.service.enums.Role;
import com.ecomhub.user.service.repository.ProfileRepository;
import com.ecomhub.user.service.repository.AccountRepository;
import com.ecomhub.security.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OtpService otpService;


    public RegisterResponse register(RegisterRequest request) {
        Optional<Account> account = accountRepository.findByEmail(request.getEmail());

        if (account.isPresent())
            throw new AccountAlreadyExistException("account already exist with email: " + request.getEmail());

        Account newAccount = new Account();
        newAccount.setEmail(request.getEmail());
        newAccount.setPassword(encoder.encode(request.getPassword()));
        newAccount.setRole(request.isSeller() ? Role.SELLER : Role.CUSTOMER);

        Account savedAccount = accountRepository.save(newAccount);
        log.info("account registered successfully: {}", savedAccount);

        otpService.sendOtp(savedAccount.getEmail());

        RegisterResponse response = new RegisterResponse();
        response.setId(savedAccount.getId());
        response.setEmail(savedAccount.getEmail());

        return response;
    }

    public LoginResponse login(LoginRequest request) {

        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AccountNotFoundException("account not found with email: " + request.getEmail()));

        if (!encoder.matches(request.getPassword(), account.getPassword())) {
            throw new InvalidCredentialsException("invalid credentials");
        }

        if(!account.isVerified()){
            throw new InvalidCredentialsException("please verify your email first.");
        }

        String token = jwtService.generateToken(account.getId(), account.getEmail(), account.getRole().toString());

        LoginResponse response = new LoginResponse();
        response.setToken(token);

        return response;
    }

    public void verifyAccount(String email, String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);

        if (isValid) {
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with email: " + email));
            account.setVerified(true);
            accountRepository.save(account);

            log.info("OTP verified successfully for email: {}", email);
        }
    }

}
