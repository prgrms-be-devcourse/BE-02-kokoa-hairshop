package com.prgms.kokoahairshop.user.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.kokoahairshop.user.dto.LoginRequestDto;
import com.prgms.kokoahairshop.user.dto.RegisterRequestDto;
import com.prgms.kokoahairshop.user.dto.TokenResponseDto;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs()
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static String accessToken = "";


    @Test
    @Order(1)
    @DisplayName("회원가입테스트")
    void SINGUP_TEST() throws Exception {
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
    void LOGIN_TEST() throws Exception {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
            .email("test@gmail.com")
            .password("test1234")
            .build();

        ResultActions resultActions = mockMvc.perform(post("/login")
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
                    fieldWithPath("accessToken").type(JsonFieldType.STRING)
                        .description("accessToken")
                )
            ));

        // Response Body에서 Access Token 빼오기
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        TokenResponseDto response = objectMapper.readValue(contentAsString, TokenResponseDto.class);

        accessToken = response.getAccessToken();


    }

    @Test
    @DisplayName("인증테스트")
    void ME_TEST() throws Exception {

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

    @AfterAll
    @DisplayName("테스트 데이터 모두삭제")
    void ROLL_BACK() {
        userRepository.deleteAll();
        List<User> all = userRepository.findAll();
        assertThat(all.isEmpty(), is(true));
    }
}