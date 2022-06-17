package com.example.crud.repository;

import com.example.crud.model.Comment;
import com.example.crud.model.Post;
import com.example.crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface CommentRepository  extends JpaRepository<Comment,Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
