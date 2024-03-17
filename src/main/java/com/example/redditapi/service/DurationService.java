package com.example.redditapi.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
public class DurationService {
    public String getDuration(Instant time) { 
        Instant currentTime = Instant.now();

        if (time.equals(currentTime)) {
            return "just now";
        }
        
        Duration duration = Duration.between(time, currentTime);
        
        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        long weeks = duration.toDays() / 7;
        long months = duration.toDays() / 30;
        long years = duration.toDays() / 365;
        
        if (years > 0) {
            return years + " year" + (years > 1 ? "s" : "") + " ago";
        } else if (months > 0) {
            return months + " month" + (months > 1 ? "s" : "") + " ago";
        } else if (weeks > 0) {
           return weeks + " week" + (weeks > 1 ? "s" : "") + " ago";
        } else if (days > 0) {
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (minutes > 0) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else {
            return seconds + " second" + (seconds > 1 ? "s" : "") + " ago";
        }
    }
}
