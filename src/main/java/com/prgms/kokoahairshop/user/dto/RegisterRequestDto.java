package com.prgms.kokoahairshop.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterRequestDto {

    @Email(message = "Different from email format")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotBlank(message = "auth cannot be blank")
    private String auth;

}
