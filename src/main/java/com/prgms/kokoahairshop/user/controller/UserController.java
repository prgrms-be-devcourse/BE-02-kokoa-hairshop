package com.prgms.kokoahairshop.user.controller;


import com.prgms.kokoahairshop.user.dto.LoginRequestDto;
import com.prgms.kokoahairshop.user.dto.RegisterRequestDto;
import com.prgms.kokoahairshop.user.dto.RegisterResponseDto;
import com.prgms.kokoahairshop.user.dto.TokenResponseDto;
import com.prgms.kokoahairshop.user.dto.UserInfoDto;
import com.prgms.kokoahairshop.user.service.UserDetailService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private UserDetailService userDetailService;

    public UserController(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDto> signup(
        @RequestBody @Valid RegisterRequestDto registerRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailService.register(
            registerRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
        @RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok().body(userDetailService.login(loginRequestDto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> me() {
        return ResponseEntity.ok().body(userDetailService.userInfo());
    }


}

