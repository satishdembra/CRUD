package com.example.crud.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class SubredditDto {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;

}
