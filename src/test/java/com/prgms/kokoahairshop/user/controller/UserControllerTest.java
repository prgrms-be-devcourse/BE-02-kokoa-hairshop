package com.prgms.kokoahairshop.user.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.kokoahairshop.user.dto.LoginRequestDto;
import com.prgms.kokoahairshop.user.dto.RegisterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Order(1)
    @DisplayName("회원가입테스트")
    void signup_test() throws Exception {
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
            .email("test@gmail.com")
            .password("test1234")
            .auth("USER")
            .build();

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDto)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("sign-up",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("password"),
                    fieldWithPath("auth").type(JsonFieldType.STRING).description("auth")
                ),
                responseFields(
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId")
                )
            ));

    }

    @Test
    @Order(2)
    @DisplayName("로그인테스트")
    void login_test() throws Exception {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
            .email("test@gmail.com")
            .password("test1234")
            .build();

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("login",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("password")
                ),
                responseFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING).description("accessToken")
                )
            ));

    }

    @Test
    @DisplayName("인증테스트")
    void me_test() throws Exception {
        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTY1NjQ5NTU0NSwidXNlck5hbWUiOiJ0ZXN0QGdtYWlsLmNvbSJ9.872qTthWz1Tn-FHjEDh0Z2pqpUU5jcAy4MscJW3H4YA9x1IyIpNOblZJdEdpsZ1WKPNUwOfn2sP-caH1GZgLRg";

        mockMvc.perform(get("/me")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("me",
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
                    fieldWithPath("auth").type(JsonFieldType.STRING).description("auth")
                )
            ));


    }
}