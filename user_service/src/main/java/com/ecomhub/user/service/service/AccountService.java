package com.ecomhub.user.service.service;

import com.ecomhub.user.service.dto.request.RegisterRequest;
import com.ecomhub.user.service.dto.request.LoginRequest;
import com.ecomhub.user.service.dto.response.LoginResponse;
import com.ecomhub.user.service.dto.response.RegisterResponse;
import com.ecomhub.user.service.entity.Account;
import com.ecomhub.user.service.exception.InvalidCredentialsException;
import com.ecomhub.user.service.exception.UserAlreadyExistException;
import com.ecomhub.user.service.exception.UserNotFoundException;
import com.ecomhub.user.service.entity.UserProfile;
import com.ecomhub.user.service.entity.enums.Role;
import com.ecomhub.user.service.repository.UserProfileRepository;
import com.ecomhub.user.service.repository.AccountRepository;
import com.ecomhub.user.service.service.jwt.JwtService;
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
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        Optional<Account> user = accountRepository.findByEmail(request.getEmail());

        if (user.isPresent())
            throw new UserAlreadyExistException("User already exist with email: " + request.getEmail());

        Role role = request.isSeller() ? Role.SELLER : Role.CUSTOMER;

        Account newAccount = new Account();
        newAccount.setEmail(request.getEmail());
        newAccount.setPassword(encoder.encode(request.getPassword()));
        newAccount.setRole(role);

        Account savedAccount = accountRepository.save(newAccount);

        // Create user profile
        createUserProfile(savedAccount);

        RegisterResponse response = new RegisterResponse();
        response.setId(savedAccount.getId());
        response.setEmail(savedAccount.getEmail());

        return response;
    }

    public LoginResponse login(LoginRequest request) {

        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        LoginResponse response = new LoginResponse();

        if (!encoder.matches(request.getPassword(), account.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(account.getEmail());
        response.setToken(token);

        return response;
    }

    public void createUserProfile(Account account){
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(account.getEmail().split("@")[0]);
        userProfile.setEmail(account.getEmail());
        userProfile.setAccount(account);
        userProfileRepository.save(userProfile);
        log.info("User profile created successfully for user: {}", account.getEmail());
    }

}
