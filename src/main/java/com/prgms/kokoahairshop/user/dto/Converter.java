package com.prgms.kokoahairshop.user.dto;

import com.prgms.kokoahairshop.user.entity.User;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor
public class Converter {

    public User registerRequestToEntity(RegisterRequestDto registerRequestDto) {
        return User.builder()
            .email(registerRequestDto.getEmail())
            .password(registerRequestDto.getPassword())
            .auth(registerRequestDto.getAuth())
            .build();
    }

    public UserInfoDto entityToUserInfoDto(User user) {
        return UserInfoDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .auth(user.getAuth())
            .build();
    }

    public RegisterResponseDto toRegisterResponse(Long id) {
        return RegisterResponseDto.builder()
            .userId(id)
            .build();
    }

}
