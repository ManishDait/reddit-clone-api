package com.example.redditapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.redditapi.dto.RegisterRequest;
import com.example.redditapi.dto.AuthResponse;
import com.example.redditapi.dto.LoginRequest;
import com.example.redditapi.dto.RefereshTokenRequest;
import com.example.redditapi.service.AuthService;
import com.example.redditapi.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest request) {
        authService.singup(request);
        return new ResponseEntity<>("User created sucessfully", HttpStatus.OK);
    }

    @GetMapping("/accountverification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated sucessfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    } 

    @PostMapping("/refresh/token")
    public AuthResponse resfershToken(@Valid @RequestBody RefereshTokenRequest request) {
        return authService.refereshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout (@RequestBody RefereshTokenRequest request) {
        refreshTokenService.deleteRefreshToken(request.getRefershToken());
        return ResponseEntity.status(HttpStatus.OK).body("Logout sucessfull");
    }
    

}
