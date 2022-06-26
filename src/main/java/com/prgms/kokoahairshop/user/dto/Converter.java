package com.prgms.kokoahairshop.user.dto;

import com.prgms.kokoahairshop.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
public class Converter<T> {

    public User registerDtoToEntity(RegisterUserDto registerUserDto) {
        return User.builder()
            .email(registerUserDto.getEmail())
            .password(registerUserDto.getPassword())
            .auth(registerUserDto.getAuth())
            .build();
    }

    public UserInfoDto entityToUserInfoDto(User user) {
        return UserInfoDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .auth(user.getAuth())
            .build();
    }

}
