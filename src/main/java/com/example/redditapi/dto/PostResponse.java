package com.example.redditapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostResponse {
    private Long id;
    private String subredditName;
    private String postName;
    private String description;
    private String url;
    private String username;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
    private boolean isUpVote;
    private boolean isDownVote;
}
