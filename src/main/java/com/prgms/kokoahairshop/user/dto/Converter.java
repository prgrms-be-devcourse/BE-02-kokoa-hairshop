package com.prgms.kokoahairshop.user.dto;

import com.prgms.kokoahairshop.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Converter {
    public User registerDtoToEntity(RegisterUserDto registerUserDto){
        return User.builder()
        .email(registerUserDto.getEmail())
        .password(registerUserDto.getPassword())
        .auth(registerUserDto.getAuth())
            .build();
    }
}
