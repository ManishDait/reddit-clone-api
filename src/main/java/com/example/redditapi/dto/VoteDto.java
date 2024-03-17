package com.example.redditapi.dto;

import com.example.redditapi.model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private Long postId;
    private VoteType voteType;
}
