package com.example.redditapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.redditapi.error.RedditApiException;
import com.example.redditapi.model.User;
import com.example.redditapi.repositoriy.UserRepository;

@Component
public class ApplicationUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RedditApiException("Username not available with name: " + username));
        return new ApplicationUser(user);
    }

}
