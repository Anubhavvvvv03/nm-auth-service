package com.example.nm_auth_service.dto;

import com.example.nm_auth_service.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String phone;
    private String password;
    private Role role;

}
