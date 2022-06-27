package com.prgms.kokoahairshop.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterRequestDto {

    @Email(message = "Different from email format")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotBlank(message = "auth cannot be blank")
    private String auth;

}
