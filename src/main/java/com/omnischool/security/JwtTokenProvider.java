package com.omnischool.security;

import com.omnischool.enums.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.util.Date;

/**
 * JWT generator/validator.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(deriveHmacKey(secret));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return buildToken(user.getUsername(), user.getUser().getRole(), accessTokenExpirationMs);
    }

    public String generateRefreshToken(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return buildToken(user.getUsername(), user.getUser().getRole(), refreshTokenExpirationMs);
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    public UserRole getRoleFromToken(String token) {
        Object role = parseClaims(token).getBody().get("role");
        return role == null ? null : UserRole.valueOf(role.toString());
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private String buildToken(String email, UserRole role, long expirationMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    /**
     * Ensures we always end up with >= 256-bit key material for HS256.
     *
     * <p>Behavior:
     * <ul>
     *   <li>If {@code secret} is valid Base64, decode it into bytes.</li>
     *   <li>Otherwise, use UTF-8 bytes of the string.</li>
     *   <li>If resulting byte[] is too short, hash it with SHA-256 (32 bytes).</li>
     * </ul>
     */
    private static byte[] deriveHmacKey(String secret) {
        byte[] raw;

        if (secret != null && secret.matches("^[A-Za-z0-9+/=\\r\\n]+$")) {
            try {
                raw = Decoders.BASE64.decode(secret);
            } catch (Exception ex) {
                raw = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            }
        } else {
            raw = (secret == null ? "" : secret).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }

        // HS256 requires at least 256 bits (32 bytes).
        if (raw.length >= 32) {
            return raw;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(raw);
        } catch (Exception e) {
            // Fallback: pad with zeros (should never happen).
            return java.util.Arrays.copyOf(raw, 32);
        }
    }
}
