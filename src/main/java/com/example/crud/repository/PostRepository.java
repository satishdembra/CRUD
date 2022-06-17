package com.example.crud.repository;

import com.example.crud.model.Post;
import com.example.crud.model.Subreddit;
import com.example.crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
