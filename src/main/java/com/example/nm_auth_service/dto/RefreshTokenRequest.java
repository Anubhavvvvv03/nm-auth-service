package com.example.nm_auth_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
