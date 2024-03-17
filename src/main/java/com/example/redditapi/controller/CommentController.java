package com.example.redditapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.redditapi.dto.CommentDto;
import com.example.redditapi.service.CommentService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;
    
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto request) {
        commentService.createComment(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{id}")
    public ResponseEntity<List<CommentDto>> getCommentByPost(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentByPost(id));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentDto>> getCommentByUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentByUser(username));
    }
    

}
