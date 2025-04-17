package com.example.nm_auth_service.service;

import com.example.nm_auth_service.dto.*;
import com.example.nm_auth_service.model.User;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface AuthService {

    Optional<User> loadUserByUserId(Integer userId);

    LoginResponse login(LoginRequest request);

    @Transactional
    RegisterResponse register(RegisterRequest request);


    JwtResponse refreshToken(String refreshToken);

}
