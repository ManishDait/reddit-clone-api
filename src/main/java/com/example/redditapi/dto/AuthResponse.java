package com.example.redditapi.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String authToken;
    private String refereshToken;
    private Instant expiresAt;
    private String username;
}
