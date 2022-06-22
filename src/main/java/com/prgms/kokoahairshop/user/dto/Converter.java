package com.prgms.kokoahairshop.user.dto;

import com.prgms.kokoahairshop.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class Converter {
    public User registerDtoToEntity(RegisterUserDto registerUserDto){
        return new User(registerUserDto.getEmail(), registerUserDto.getPassword(), registerUserDto.getRole());
    }
}
