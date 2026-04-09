package com.gametech.platform.common.security;

import com.gametech.platform.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${gametech.security.jwt-secret}")
    private String jwtSecret;

    @Value("${gametech.security.token-expire-seconds}")
    private long tokenExpireSeconds;

    private Key signingKey;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String role, String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenExpireSeconds * 1000);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtUser parseToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            JwtUser jwtUser = new JwtUser();
            jwtUser.setUserId(Long.valueOf(claims.getSubject()));
            jwtUser.setRole(String.valueOf(claims.get("role")));
            jwtUser.setUsername(String.valueOf(claims.get("username")));
            return jwtUser;
        } catch (JwtException | IllegalArgumentException exception) {
            throw new BusinessException("invalid or expired token");
        }
    }
}
