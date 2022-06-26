package com.prgms.kokoahairshop.user.controller;


import com.prgms.kokoahairshop.user.dto.LoginUserDto;
import com.prgms.kokoahairshop.user.dto.RegisterUserDto;
import com.prgms.kokoahairshop.user.dto.TokenResponseDto;
import com.prgms.kokoahairshop.user.dto.UserInfoDto;
import com.prgms.kokoahairshop.user.service.UserDetailService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Long> signup(@RequestBody @Valid RegisterUserDto registerUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailService.register(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginUserDto loginUserDto) {
        return ResponseEntity.ok().body(userDetailService.login(loginUserDto));
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserInfoDto> test() {
        return ResponseEntity.ok().body(userDetailService.userInfo());
    }

}

