package com.example.redditapi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditapi.dto.VoteDto;
import com.example.redditapi.error.PostNotFoundException;
import com.example.redditapi.error.RedditApiException;
import com.example.redditapi.model.Post;
import com.example.redditapi.model.User;
import com.example.redditapi.model.Vote;
import com.example.redditapi.model.VoteType;
import com.example.redditapi.repositoriy.PostRepository;
import com.example.redditapi.repositoriy.VoteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    private final AuthService authService;

    @Transactional
    public void votePost(VoteDto request) {
        Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> new PostNotFoundException(request.getPostId()));
        User user = authService.getCurrentUser();
        Optional<Vote> prevVote = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, user);
        log.info("Vote: ", prevVote);

        if (prevVote.isPresent() && prevVote.get().getVoteType().equals(request.getVoteType())) {
            throw new RedditApiException("Vote is allow one time");
        }

        if (request.getVoteType().equals(VoteType.UPVOTE)) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        Vote vote = Vote.builder().post(post)
            .user(user)
            .voteType(request.getVoteType())
            .build();

        voteRepository.save(vote);
        postRepository.save(post);
    }
    
}
