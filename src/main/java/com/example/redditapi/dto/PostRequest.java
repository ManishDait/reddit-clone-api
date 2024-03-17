package com.example.redditapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostRequest {
    private Long id;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
}
