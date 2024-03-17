package com.example.redditapi.repositoriy;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.redditapi.model.Post;
import com.example.redditapi.model.Subreddit;
import com.example.redditapi.model.User;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findAllBySubreddit(Subreddit subreddit);
    Optional<List<Post>> findAllByUser(User user);
}
