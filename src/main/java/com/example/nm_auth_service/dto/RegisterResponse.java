package com.example.nm_auth_service.dto;

import com.example.nm_auth_service.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterResponse {

    private String username;
    private String email;
    private String phone;
    private JwtResponse jwtResponse;
    private Role role;


    public RegisterResponse(String username,String email,String phone,JwtResponse jwtResponse,Role role) {
        this.username = username;
        this.email=email;
        this.phone=phone;
        this.jwtResponse = jwtResponse;
        this.role=role;
    }
}
