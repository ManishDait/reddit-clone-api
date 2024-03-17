package com.example.redditapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.redditapi.dto.PostRequest;
import com.example.redditapi.dto.PostResponse;
import com.example.redditapi.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPost(@Nullable @RequestParam(required = false, defaultValue = "") String user) {
        if (user == null) {
            user = "";
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getAllPost(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id, @Nullable @RequestParam String user) {
        if (user == null) {
            user = "";
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPost(id, user));
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostBySubreddit(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPostBySubreddit(id));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(postService.getPostByUsername(username));
    }

}
