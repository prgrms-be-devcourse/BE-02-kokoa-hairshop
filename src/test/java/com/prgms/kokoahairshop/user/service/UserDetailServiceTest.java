package com.prgms.kokoahairshop.user.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


import com.prgms.kokoahairshop.jwt.JwtAuthenticationProvider;
import com.prgms.kokoahairshop.user.dto.LoginRequestDto;
import com.prgms.kokoahairshop.user.dto.RegisterRequestDto;
import com.prgms.kokoahairshop.user.dto.RegisterResponseDto;
import com.prgms.kokoahairshop.user.dto.TokenResponseDto;
import com.prgms.kokoahairshop.user.exception.EmailAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDetailServiceTest {

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    JwtAuthenticationProvider jwtAuthenticationProvider;

    @Test
    @DisplayName("이메일,비밀번호,권한정보로 회원가입을 할 수 있다")
    @Order(1)
    void register_test(){
        // given
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                                                    .email("test@gmail.com")
                                                    .password("test1234")
                                                    .auth("ADMIN")
                                                    .build();

        // when
        RegisterResponseDto responseDto = userDetailService.register(registerRequestDto);

        // then
        assertThat(responseDto.getUserId(), is(1L));

    }

    @Test
    @Order(2)
    @DisplayName("이메일,비밀번호로 로그인을 할 수 있다.")
    void login_test() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                                            .email("test@gmail.com")
                                            .password("test1234")
                                            .build();

        // when
        TokenResponseDto token = userDetailService.login(loginRequestDto);

        // then
        assertThat(jwtAuthenticationProvider.getUserNameFromToken(token.getAccessToken()), is("test@gmail.com"));

    }

    @Test
    @DisplayName("같은 이메일로 회원가입을 할 수 없다")
    @Order(3)
    void email_already_exist_test(){
        // given
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
            .email("test@gmail.com")
            .password("test1234")
            .auth("USER")
            .build();

        // when
        assertThrows(EmailAlreadyExistException.class, () -> userDetailService.register(
            registerRequestDto));


    }

    @Test
    @DisplayName("없는 이메일로 로그인시도")
    @Order(4)
    void email_doesnt_matching_test(){
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
            .email("test1@gmail.com")
            .password("test1234")
            .build();

        // when
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.login(loginRequestDto));


    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도")
    @Order(5)
    void password_doesnt_matching_test(){
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
            .email("test@gmail.com")
            .password("test12")
            .build();

        // when
        assertThrows(IllegalArgumentException.class, () -> userDetailService.login(loginRequestDto));



    }


}