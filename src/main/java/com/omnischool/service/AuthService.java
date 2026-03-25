package com.omnischool.service;

import com.omnischool.dto.LoginRequest;
import com.omnischool.dto.LoginResponse;
import com.omnischool.dto.RefreshTokenResponse;
import com.omnischool.dto.RegisterAdminRequest;
import com.omnischool.dto.UserDTO;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(String refreshToken);

    UserDTO registerAdmin(RegisterAdminRequest request);
}

