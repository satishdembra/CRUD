package com.example.crud.mapper;

import com.example.crud.dto.SubredditDto;
import com.example.crud.model.Post;
import com.example.crud.model.Subreddit;
import lombok.AllArgsConstructor;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SubredditMappper {
    @Mapping(target = "numberOfPosts",expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts){return numberOfPosts.size();}
    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
