package com.example.nm_auth_service.dto;

import com.example.nm_auth_service.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class JwtResponse {

    private String jwtToken;
    private String refreshToken;

    public JwtResponse(String jwtToken, String refreshToken,User user) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;

    }

    // Getters and Setters
}
