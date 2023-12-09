package com.microservice.glologic.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private long expiration;
    private Key key = MacProvider.generateKey(SignatureAlgorithm.HS512);

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expirationInstant = now.plus(expiration, ChronoUnit.MILLIS);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationInstant))
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String username, String token) {
        String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractClaims(token).getExpiration();
        return expirationDate.before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token).getBody();
    }

}
