package com.example.redditapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.redditapi.dto.SubredditDto;
import com.example.redditapi.service.SubredditServie;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SubredditController {

    private final SubredditServie subredditServie;
    
    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(subredditServie.createSubreddit(request));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddit() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(subredditServie.getAllSubreddit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(subredditServie.getSubreddit(id));
    }

}
