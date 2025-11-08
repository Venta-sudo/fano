package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final MeterRegistry meterRegistry;

    public AuthController(AuthService authService, MeterRegistry meterRegistry) {
        this.authService = authService;
        this.meterRegistry = meterRegistry;
        
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody RegisterRequest registerRequest
    ) {
        String response = authService.register(registerRequest);
        meterRegistry.counter("users").increment();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
        @RequestBody LoginRequest loginRequest
    ) {
        String response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
