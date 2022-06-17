package com.example.crud.dto;

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class AuthDto {
    private String authenticationToken;
    private String username;
    private Instant expiry;
    private String refreshToken;


}
