package com.example.crud.repository;

import com.example.crud.model.Comment;
import com.example.crud.model.Post;
import com.example.crud.model.User;
import com.example.crud.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
    List<Vote> findByPost(Post post);
}
