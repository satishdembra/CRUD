package com.example.crud.dto.Request;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Register {
    private String email;
    private String username;
    private String password;

}
