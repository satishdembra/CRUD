package com.example.crud.repository;

import com.example.crud.model.Comment;
import com.example.crud.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {
}
