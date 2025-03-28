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
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        Optional<Account> account = accountRepository.findByEmail(request.getEmail());

        if (account.isPresent())
            throw new AccountAlreadyExistException("account already exist with email: " + request.getEmail());

        Role role = request.isSeller() ? Role.SELLER : Role.CUSTOMER;

        Account newAccount = new Account();
        newAccount.setEmail(request.getEmail());
        newAccount.setPassword(encoder.encode(request.getPassword()));
        newAccount.setRole(role);

        Account savedAccount = accountRepository.save(newAccount);
        log.info("account registered successfully: {}", savedAccount);

        // Create user profile
        createUserProfile(savedAccount);

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

        LoginResponse response = new LoginResponse();

        String token = jwtService.generateToken(account.getEmail(), account.getRole().toString());
        response.setToken(token);

        return response;
    }

    public void createUserProfile(Account account){
        Profile profile = new Profile();
        profile.setUsername(account.getEmail().split("@")[0]);
        profile.setEmail(account.getEmail());
        profile.setAccount(account);
        profileRepository.save(profile);
        log.info("profile created successfully for account: {}", account.getEmail());
    }

}
