package com.example.redditapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefereshTokenRequest {
    @NotBlank
    private String refershToken;
    private String username;
}
