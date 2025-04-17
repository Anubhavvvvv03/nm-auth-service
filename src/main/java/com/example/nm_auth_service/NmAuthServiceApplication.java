package com.example.nm_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class NmAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NmAuthServiceApplication.class, args);
	}

}
