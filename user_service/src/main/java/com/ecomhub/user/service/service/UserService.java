package com.ecomhub.user.service.service;

import com.ecomhub.user.service.dto.request.RegisterRequest;
import com.ecomhub.user.service.dto.request.LoginRequest;
import com.ecomhub.user.service.dto.response.LoginResponse;
import com.ecomhub.user.service.dto.response.RegisterResponse;
import com.ecomhub.user.service.exception.InvalidCredentialsException;
import com.ecomhub.user.service.exception.UserAlreadyExistException;
import com.ecomhub.user.service.exception.UserNotFoundException;
import com.ecomhub.user.service.model.User;
import com.ecomhub.user.service.model.enums.Role;
import com.ecomhub.user.service.repository.UserRepository;
import com.ecomhub.user.service.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent())
            throw new UserAlreadyExistException("User already exist with email: " + request.getEmail());

        Role role = request.isSeller() ? Role.SELLER : Role.CUSTOMER;

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encoder.encode(request.getPassword()));
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);

        RegisterResponse response = new RegisterResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());

        return response;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        LoginResponse response = new LoginResponse();

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        response.setToken(token);

        return response;
    }

}
