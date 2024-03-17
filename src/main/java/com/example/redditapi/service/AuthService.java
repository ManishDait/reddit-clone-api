package com.example.redditapi.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditapi.dto.RegisterRequest;
import com.example.redditapi.config.ApplicationUser;
import com.example.redditapi.dto.AuthResponse;
import com.example.redditapi.dto.LoginRequest;
import com.example.redditapi.dto.RefereshTokenRequest;
import com.example.redditapi.error.RedditApiException;
import com.example.redditapi.model.NotificationEmail;
import com.example.redditapi.model.User;
import com.example.redditapi.model.VerificationToken;
import com.example.redditapi.repositoriy.UserRepository;
import com.example.redditapi.repositoriy.VerificationTokenRepository;
import com.example.redditapi.security.JwtProvider;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final MailService mailService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void singup(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setSubject("Please Activate your Account");
        notificationEmail.setRecipent(user.getEmail());
        notificationEmail.setBody("Please Activate your Reddit Clone Account by clicking on below urt \n http://localhost:8080/api/auth/accountverification/"+token);
        mailService.sendMail(notificationEmail);
    }

    @Transactional
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new RedditApiException("Invalid token"));
        enableUser(verificationToken.get());
    }

    @Transactional
    private void enableUser(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user =  userRepository.findByUsername(username).orElseThrow(() -> new RedditApiException("Username not fond with username: " + username));

        user.setEnabled(true);
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User principal: {}", authentication.getPrincipal());
        if ((authentication.getPrincipal() instanceof String)) {
            return null;
        }
        ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();
        return userRepository.findByUsername(applicationUser.getUsername()).orElseThrow(() -> new RedditApiException("Username not fond with username: " + applicationUser.getUsername()));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new RedditApiException("Invalid Credentials");
        }
        log.info(authentication.getName());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authToken = jwtProvider.generateToken(request.getUsername());
        log.info("Set context: {}", SecurityContextHolder.getContext().getAuthentication().getClass());

        AuthResponse response = new AuthResponse();
        response.setAuthToken(authToken);
        response.setUsername(request.getUsername());
        response.setExpiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTime()));
        response.setRefereshToken(refreshTokenService.generateRefershToken().getToken());

        return response;
    }

    public AuthResponse refereshToken(@Valid RefereshTokenRequest request) {
        refreshTokenService.validateRefreshToken(request.getRefershToken());
        String token = jwtProvider.generateToken(request.getUsername());
        AuthResponse response = new AuthResponse();
        response.setAuthToken(token);
        response.setRefereshToken(request.getRefershToken());
        response.setUsername(request.getUsername());
        response.setExpiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTime()));

        return response;
    }
}
