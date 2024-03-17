package com.example.redditapi.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.redditapi.dto.VoteDto;
import com.example.redditapi.service.VoteService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/vote")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class VoteController {
    
    private final VoteService voteService;

    @PostMapping
    public void votePost(@RequestBody VoteDto request) {
        voteService.votePost(request);
    }

}
