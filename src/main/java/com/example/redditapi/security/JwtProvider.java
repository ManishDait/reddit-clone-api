package com.example.redditapi.security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
    
    private final String KEY = "815F1298534CA2989B84C1C2F48D4609E7A05811E0700653661466A6";
    @Value("${jwt.expiration.time}")
    private Long expirationInMillis;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
           .setClaims(claims)
           .setSubject(username)
           .setIssuedAt(new Date(System.currentTimeMillis()))
           .setExpiration(Date.from(Instant.now().plusMillis(expirationInMillis)))
           .signWith(getPrivateKey(), SignatureAlgorithm.HS256)
           .compact();
    }

    public boolean validateToken(String jwt) {
        Jwts.parserBuilder().setSigningKey(getPrivateKey()).build().parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getPrivateKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    private Key getPrivateKey() {
        byte[] keyByte = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public Long getExpirationTime() {
        return this.expirationInMillis;
    }

}
