package com.example.crud.service;

import com.example.crud.Exception.CRUDException;
import com.example.crud.dto.SubredditDto;
import com.example.crud.mapper.SubredditMappper;
import com.example.crud.model.Subreddit;
import com.example.crud.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save = subredditRepository.save( mapSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    private Subreddit mapSubreddit(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }

    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder().name(subreddit.getName()).description(subreddit.getDescription()).id(subreddit.getId()).numberOfPosts(subreddit.getPosts().size()).build();

    }
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(
                () -> new CRUDException("No Subreddit Found against this ID")

        );
        return mapToDto(subreddit);
    }
}
