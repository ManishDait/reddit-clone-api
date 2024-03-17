package com.example.redditapi.error;

public class SubredditNotFoundException extends RuntimeException {
    public SubredditNotFoundException(String name) {
        super("No subreddit found with name: " + name);
    }

    public SubredditNotFoundException(Long id) {
        super("No subreddit found with id: " + id);
    }
}
