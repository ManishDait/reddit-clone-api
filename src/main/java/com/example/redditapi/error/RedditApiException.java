package com.example.redditapi.error;

public class RedditApiException extends RuntimeException {
    public RedditApiException(String message) {
        super(message);
    }
}
