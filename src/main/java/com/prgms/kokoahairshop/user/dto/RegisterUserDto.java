package com.prgms.kokoahairshop.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserDto {

    private String email;
    private String password;
    private String role;

}
