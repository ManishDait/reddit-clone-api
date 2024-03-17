package com.example.redditapi.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditapi.dto.PostRequest;
import com.example.redditapi.dto.PostResponse;
import com.example.redditapi.error.PostNotFoundException;
import com.example.redditapi.error.RedditApiException;
import com.example.redditapi.error.SubredditNotFoundException;
import com.example.redditapi.model.Post;
import com.example.redditapi.model.Subreddit;
import com.example.redditapi.model.User;
import com.example.redditapi.model.Vote;
import com.example.redditapi.model.VoteType;
import com.example.redditapi.repositoriy.CommentRpository;
import com.example.redditapi.repositoriy.PostRepository;
import com.example.redditapi.repositoriy.SubredditRepository;
import com.example.redditapi.repositoriy.UserRepository;
import com.example.redditapi.repositoriy.VoteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {
    
    private final AuthService authService;

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final SubredditRepository subredditRepository;
    private final CommentRpository commentRpository;

    private final DurationService durationService;

    @Transactional
    public PostResponse createPost(PostRequest request) {
        Subreddit subreddit = subredditRepository.findByName(request.getSubredditName()).orElseThrow(() -> new SubredditNotFoundException(request.getSubredditName()));
        User user = authService.getCurrentUser();
        
        Post post = Post.builder().postName(request.getPostName())
            .description(request.getDescription())
            .url(request.getUrl())
            .user(user)
            .voteCount(0)
            .subreddit(subreddit)
            .createdDate(Instant.now())
            .build();

        Post response = postRepository.save(post);
        subreddit.getPosts().add(response);
        subredditRepository.save(subreddit);
        return mapToPostResponse(response);
    }

    @Transactional
    public List<PostResponse> getAllPost(String user) {
        return postRepository.findAll().stream().map(p -> mapToPostResponse(p, user)).collect(Collectors.toList());
    }

    @Transactional
    public PostResponse getPost(Long id, String user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        return mapToPostResponse(post, user);
    }

    @Transactional
    public List<PostResponse> getPostBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SubredditNotFoundException(id));
        return postRepository.findAllBySubreddit(subreddit).get().stream().map(p -> mapToPostResponse(p)).collect(Collectors.toList());
    }

    @Transactional
    public List<PostResponse> getPostByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RedditApiException("No user found with username: " + username));
        return postRepository.findAllByUser(user).get().stream().map(p -> mapToPostResponse(p)).collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post p) {
        return PostResponse.builder().id(p.getPostId())
            .postName(p.getPostName())
            .description(p.getDescription())
            .subredditName(p.getSubreddit().getName())
            .url(p.getUrl())
            .commentCount(commentRpository.findAllByPost(p).get().size())
            .voteCount(p.getVoteCount())
            .duration(durationService.getDuration((p.getCreatedDate())))
            .username(p.getUser().getUsername())
            .isDownVote(false)
            .isUpVote(false)
            .build();
    }

    private PostResponse mapToPostResponse(Post p, String user) {
        if(user == null || user.equals("")) {
            return mapToPostResponse(p);
        }
        Optional<User> currentUser = userRepository.findByUsername(user);
        if (!currentUser.isPresent()) {
            mapToPostResponse(p);
        }

        return PostResponse.builder().id(p.getPostId())
            .postName(p.getPostName())
            .description(p.getDescription())
            .subredditName(p.getSubreddit().getName())
            .url(p.getUrl())
            .commentCount(commentRpository.findAllByPost(p).get().size())
            .voteCount(p.getVoteCount())
            .duration(durationService.getDuration((p.getCreatedDate())))
            .username(p.getUser().getUsername())
            .isDownVote(checkVote(VoteType.DOWNVOTE, p, currentUser.get()))
            .isUpVote(checkVote(VoteType.UPVOTE, p, currentUser.get()))
            .build();
    }

    private boolean checkVote(VoteType voteType, Post post, User user) {
        Optional<Vote> vote = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, user);
        if (vote.isPresent() && vote.get().getVoteType().equals(voteType)) {
            return true;
        }

        return false;
    }
    
}
