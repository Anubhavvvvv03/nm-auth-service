package com.example.nm_auth_service.controller;

import com.example.nm_auth_service.dto.*;
import com.example.nm_auth_service.service.AuthService;
import com.example.nm_auth_service.service.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
      return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    //Can be done in login api only
//    @PostMapping("/access-token")
//    public JwtResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
//        return authService.refreshToken(refreshTokenRequest);
//    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }
}