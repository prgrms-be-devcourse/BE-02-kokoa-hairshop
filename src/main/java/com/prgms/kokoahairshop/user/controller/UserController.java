package com.prgms.kokoahairshop.user.controller;


import com.prgms.kokoahairshop.user.dto.LoginUserDto;
import com.prgms.kokoahairshop.user.dto.RegisterUserDto;
import com.prgms.kokoahairshop.user.dto.TokenResponseDto;
import com.prgms.kokoahairshop.user.dto.UserInfoDto;
import com.prgms.kokoahairshop.user.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private UserDetailService userDetailService;

    public UserController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Long signup(@RequestBody RegisterUserDto registerUserDto) {
        return userDetailService.register(registerUserDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto login(@RequestBody LoginUserDto loginUserDto) {
        return userDetailService.login(loginUserDto);
    }

    @GetMapping("/authentication")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDto test() {
        return userDetailService.userInfo();
    }

}

