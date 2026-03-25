package com.omnischool.service.impl;

import com.omnischool.dto.LoginRequest;
import com.omnischool.dto.LoginResponse;
import com.omnischool.dto.RefreshTokenResponse;
import com.omnischool.dto.RegisterAdminRequest;
import com.omnischool.dto.UserDTO;
import com.omnischool.entity.User;
import com.omnischool.enums.UserRole;
import com.omnischool.exception.ResourceNotFoundException;
import com.omnischool.repository.UserRepository;
import com.omnischool.security.JwtTokenProvider;
import com.omnischool.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLastLogin(LocalDateTime.now());

        String token = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(toUserDTO(user))
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new ResourceNotFoundException("Invalid refresh token");
        }
        String email = tokenProvider.getEmailFromToken(refreshToken);

        // Ensure user still exists and is active.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ResourceNotFoundException("User is inactive");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email,
                null,
                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );

        String newAccess = tokenProvider.generateAccessToken(authentication);
        String newRefresh = tokenProvider.generateRefreshToken(authentication);

        return RefreshTokenResponse.builder()
                .token(newAccess)
                .refreshToken(newRefresh)
                .build();
    }

    @Transactional
    @Override
    public UserDTO registerAdmin(RegisterAdminRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User admin = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(UserRole.ADMIN)
                .isActive(true)
                .build();

        User savedAdmin = userRepository.save(admin);
        return toUserDTO(savedAdmin);
    }

    private static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}

