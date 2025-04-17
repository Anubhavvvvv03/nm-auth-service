package com.example.nm_auth_service.service.impl;

import com.example.nm_auth_service.dto.*;
import com.example.nm_auth_service.exception.AuthenticationException;
import com.example.nm_auth_service.model.RefreshToken;
import com.example.nm_auth_service.model.User;
import com.example.nm_auth_service.repository.RefreshTokenRepository;
import com.example.nm_auth_service.repository.UserRepository;
import com.example.nm_auth_service.security.JwtTokenProvider;
import com.example.nm_auth_service.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new AuthenticationException("Username already taken.");
        }

        // Create a new User
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Save the user
        userRepository.save(user);

        // Build and return RegisterResponse
        RegisterResponse response = new RegisterResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        return response;
    }

    @Override
    public Optional<User> loadUserByUserId(Integer userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            return user;
        } else {
            throw new AuthenticationException("User not found");
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // Retrieve the user from DB
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        // Check if password matches
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        // Generate JWT tokens
        String jwtToken = jwtTokenProvider.generateToken(user.getUserId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        // Store refresh token in DB
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setCreatedAt(LocalDateTime.now());
        refreshTokenEntity.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshTokenEntity);

        // Construct JwtResponse
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwtToken(jwtToken);
        jwtResponse.setRefreshToken(refreshToken);

        // Construct and return LoginResponse
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setRole(user.getRole());
        loginResponse.setJwtResponse(jwtResponse);

        return loginResponse;
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException("Invalid refresh token"));

        // Check if refresh token is expired
        if (refreshTokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Refresh token has expired");
        }

        // Generate new access token using the user associated with the refresh token
        String newJwtToken = jwtTokenProvider.generateToken(refreshTokenEntity.getUser().getUserId());

        // Build JwtResponse
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwtToken(newJwtToken);
        jwtResponse.setRefreshToken(refreshToken); // reuse existing refresh token

        return jwtResponse;
    }
}
