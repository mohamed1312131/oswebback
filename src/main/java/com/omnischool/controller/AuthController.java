package com.omnischool.controller;

import com.omnischool.dto.LoginRequest;
import com.omnischool.dto.LoginResponse;
import com.omnischool.dto.RefreshTokenResponse;
import com.omnischool.dto.RegisterAdminRequest;
import com.omnischool.dto.UserDTO;
import com.omnischool.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestHeader("Authorization") String authorization) {
        String token = authorization != null && authorization.startsWith("Bearer ")
                ? authorization.substring(7)
                : authorization;
        return ResponseEntity.ok(authService.refreshToken(token));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserDTO> registerAdmin(@Valid @RequestBody RegisterAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerAdmin(request));
    }
}

