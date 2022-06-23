package com.prgms.kokoahairshop.hairshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.kokoahairshop.hairshop.dto.HairshopDto;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.hairshop.service.HairshopService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayName("헤어샵 CRUD API 테스트")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class HairshopControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HairshopService hairshopService;

    @Autowired
    private HairshopRepository hairshopRepository;

    HairshopDto hairshopResponse;

    @BeforeEach
    void setup() {
        HairshopDto hairshopDto = HairshopDto.builder().build();
        hairshopResponse = hairshopService.insert(hairshopDto);
    }

    @AfterEach
    void tearDown() {
        hairshopRepository.deleteAll();
    }

    @Test
    @DisplayName("헤어샵 등록 테스트")
    void HAIRSHOP_INSERT_TEST() throws Exception {
        HairshopDto hairshopDto = HairshopDto.builder().build();
        this.mockMvc.perform(put("/api/v1/hairshop")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hairshopDto)))
                .andExpect(status().isCreated())
                .andDo(
                        document("register-hairshop")
                );
    }
}