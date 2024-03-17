package com.example.redditapi.error;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String name) {
        super("No post found with name: " + name);
    }

    public PostNotFoundException(Long id) {
        super("No post found with id: " + id);
    }
}
