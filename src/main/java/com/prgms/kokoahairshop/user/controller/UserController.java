package com.prgms.kokoahairshop.user.controller;


import com.prgms.kokoahairshop.user.dto.RegisterUserDto;
import com.prgms.kokoahairshop.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Long signup(@RequestBody RegisterUserDto registerUserDto) {
        return userService.register(registerUserDto);
    }

}

