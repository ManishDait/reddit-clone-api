package com.example.redditapi.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditapi.dto.CommentDto;
import com.example.redditapi.error.PostNotFoundException;
import com.example.redditapi.error.RedditApiException;
import com.example.redditapi.model.Comment;
import com.example.redditapi.model.NotificationEmail;
import com.example.redditapi.model.Post;
import com.example.redditapi.model.User;
import com.example.redditapi.repositoriy.CommentRpository;
import com.example.redditapi.repositoriy.PostRepository;
import com.example.redditapi.repositoriy.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRpository commentRpository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final AuthService authService;
    private final MailService mailService;
    private final DurationService durationService;

    @Transactional
    public void createComment(CommentDto request) {
        Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> new PostNotFoundException(request.getPostId()));
        User user = authService.getCurrentUser();
        Comment comment = Comment.builder().text(request.getText())
            .createdDate(Instant.now())
            .post(post)
            .user(user)
            .build();
        commentRpository.save(comment);

        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setRecipent(post.getUser().getEmail());
        notificationEmail.setSubject("Reaction on your post");
        notificationEmail.setBody("Hey " + post.getUser().getUsername() +", " + user.getUsername() + " has commented on your post.");

        sendNotificationEmail(notificationEmail);
    }

    public List<CommentDto> getCommentByPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        return commentRpository.findAllByPost(post).get().stream().map(c -> mapToCommentDto(c)).collect(Collectors.toList());
    }

    public List<CommentDto> getCommentByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RedditApiException("No user found with username: " + username));
        return commentRpository.findAllByUser(user).get().stream().map(c -> mapToCommentDto(c)).collect(Collectors.toList());
    }

    private void sendNotificationEmail(NotificationEmail notificationEmail) {
        mailService.sendMail(notificationEmail);
    }

    private CommentDto mapToCommentDto(Comment c) {
        return CommentDto.builder().id(c.getId())
            .text(c.getText())
            .postId(c.getPost().getPostId())
            .username(c.getUser().getUsername())
            .duration(durationService.getDuration(c.getCreatedDate()))
            .build();
    }
}
