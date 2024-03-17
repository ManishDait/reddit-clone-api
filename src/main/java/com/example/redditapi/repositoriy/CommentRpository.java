package com.example.redditapi.repositoriy;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.redditapi.model.Comment;
import com.example.redditapi.model.Post;
import com.example.redditapi.model.User;

@Repository
public interface CommentRpository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findAllByPost(Post post);
    Optional<List<Comment>> findAllByUser(User user);

}
