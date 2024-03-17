package com.example.redditapi.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.redditapi.dto.SubredditDto;
import com.example.redditapi.error.SubredditNotFoundException;
import com.example.redditapi.model.Subreddit;
import com.example.redditapi.repositoriy.SubredditRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubredditServie {
    
    private final SubredditRepository subredditRepository;

    private final AuthService authService;

    @Transactional
    public SubredditDto createSubreddit(SubredditDto request) {
        Subreddit subreddit = Subreddit.builder().name(request.getName())
            .description(request.getDescription())
            .user(authService.getCurrentUser())
            .createdDate(Instant.now())
            .build();
        
        Long id = subredditRepository.save(subreddit).getId();
        request.setId(id);
        return request;
    }
 
    @Transactional
    public List<SubredditDto> getAllSubreddit() {
        return subredditRepository.findAll()
            .stream()
            .map(e -> maptoSubredditDto(e))
            .collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SubredditNotFoundException(id));
        return maptoSubredditDto(subreddit);
    }

    private SubredditDto maptoSubredditDto(Subreddit e) {
        return SubredditDto.builder().id(e.getId())
            .name(e.getName())
            .description(e.getDescription())
            .numberOfPost(e.getPosts().size())
            .build();   
    }
    
}
