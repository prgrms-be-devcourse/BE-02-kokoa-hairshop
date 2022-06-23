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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        // TODO : 유저 기능 작성 완료 후 리팩토링
        HairshopDto hairshopDto = HairshopDto.builder()
                .userId(1L)
                .build();
        this.mockMvc.perform(put("/api/v1/hairshop")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hairshopDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(jsonPath("$.id").value(hairshopDto.getId()))
                .andDo(
                        document("register-hairshop",
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("phoneNumber"),
                                        fieldWithPath("startTime").type(JsonFieldType.STRING).description("startTime"),
                                        fieldWithPath("endTime").type(JsonFieldType.STRING).description("endTime"),
                                        fieldWithPath("closedDay").type(JsonFieldType.STRING).description("closedDay"),
                                        fieldWithPath("reservationRange").type(JsonFieldType.STRING).description("reservationRange"),
                                        fieldWithPath("reservationStartTime").type(JsonFieldType.STRING).description("reservationStartTime"),
                                        fieldWithPath("reservationEndTime").type(JsonFieldType.STRING).description("reservationEndTime"),
                                        fieldWithPath("sameDayAvailable").type(JsonFieldType.BOOLEAN).description("sameDayAvailable"),
                                        fieldWithPath("roadNameNumber").type(JsonFieldType.STRING).description("roadNameNumber"),
                                        fieldWithPath("profileImg").type(JsonFieldType.STRING).description("profileImg"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("introduction"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("id")
                                )
                        ));
    }

    @Test
    @DisplayName("전체 헤어샵 조회 테스트")
    void GET_HAIRSHOP_LIST_TEST() throws Exception {
        this.mockMvc.perform(get("/api/v1/hairshop")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andDo(document("getAll-hairshop",
                        responseFields(
                                fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("content[]"),
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("content[].phoneNumber").type(JsonFieldType.STRING).description("phoneNumber"),
                                fieldWithPath("content[].startTime").type(JsonFieldType.STRING).description("startTime"),
                                fieldWithPath("content[].endTime").type(JsonFieldType.STRING).description("endTime"),
                                fieldWithPath("content[].closedDay").type(JsonFieldType.STRING).description("closedDay"),
                                fieldWithPath("content[].reservationRange").type(JsonFieldType.STRING).description("reservationRange"),
                                fieldWithPath("content[].reservationStartTime").type(JsonFieldType.STRING).description("reservationStartTime"),
                                fieldWithPath("content[].reservationEndTime").type(JsonFieldType.STRING).description("reservationEndTime"),
                                fieldWithPath("content[].sameDayAvailable").type(JsonFieldType.BOOLEAN).description("sameDayAvailable"),
                                fieldWithPath("content[].roadNameNumber").type(JsonFieldType.STRING).description("roadNameNumber"),
                                fieldWithPath("content[].profileImg").type(JsonFieldType.STRING).description("profileImg"),
                                fieldWithPath("content[].introduction").type(JsonFieldType.STRING).description("introduction"),
                                fieldWithPath("content[].userId").type(JsonFieldType.NUMBER).description("userId"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("content[].createdAt"),
                                fieldWithPath("content[].updatedAt").type(JsonFieldType.STRING).description("content[].updatedAt"),
                                fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("pageable"),
                                fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("pageable.sort"),
                                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("pageable.sort.empty"),
                                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("pageable.sort.sorted"),
                                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("pageable.sort.unsorted"),
                                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("pageable.offset"),
                                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("pageable.pageNumber"),
                                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("pageable.pageSize"),
                                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("pageable.paged"),
                                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("pageable.unpaged"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("last"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("totalPages"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("totalElements"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("size"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("number"),
                                fieldWithPath("sort").type(JsonFieldType.OBJECT).description("sort"),
                                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("sort.empty"),
                                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("sort.sorted"),
                                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("sort.unsorted"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("first"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("numberOfElements"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("empty")
                        )
                ));
    }

    @Test
    @DisplayName("특정 헤어샵 조회 테스트")
    void GET_HAIRSHOP_BY_ID_TEST() throws Exception {
        this.mockMvc.perform(get("/api/v1/hairshop/{id}", hairshopResponse.getId())
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(jsonPath("$.id").value(hairshopResponse.getId()))
                .andExpect(jsonPath("$.name").value(hairshopResponse.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(hairshopResponse.getPhoneNumber()))
                .andExpect(jsonPath("$.startTime").value(hairshopResponse.getStartTime()))
                .andExpect(jsonPath("$.endTime").value(hairshopResponse.getEndTime()))
                .andExpect(jsonPath("$.closedDay").value(hairshopResponse.getClosedDay()))
                .andExpect(jsonPath("$.reservationRange").value(hairshopResponse.getReservationRange()))
                .andExpect(jsonPath("$.reservationStartTime").value(hairshopResponse.getReservationStartTime()))
                .andExpect(jsonPath("$.reservationEndTime").value(hairshopResponse.getReservationEndTime()))
                .andExpect(jsonPath("$.sameDayAvailable").value(hairshopResponse.getSameDayAvailable()))
                .andExpect(jsonPath("$.roadNameNumber").value(hairshopResponse.getRoadNameNumber()))
                .andExpect(jsonPath("$.profileImg").value(hairshopResponse.getProfileImg()))
                .andExpect(jsonPath("$.introduction").value(hairshopResponse.getIntroduction()))
                .andExpect(jsonPath("$.createdAt").value(hairshopResponse.getCreatedAt()))
                .andExpect(jsonPath("$.updatedAt").value(hairshopResponse.getUpdatedAt()))
                .andDo(document("getById-hairshop",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("phoneNumber"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("startTime"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("endTime"),
                                fieldWithPath("closedDay").type(JsonFieldType.STRING).description("closedDay"),
                                fieldWithPath("reservationRange").type(JsonFieldType.STRING).description("reservationRange"),
                                fieldWithPath("reservationStartTime").type(JsonFieldType.STRING).description("reservationStartTime"),
                                fieldWithPath("reservationEndTime").type(JsonFieldType.STRING).description("reservationEndTime"),
                                fieldWithPath("sameDayAvailable").type(JsonFieldType.BOOLEAN).description("sameDayAvailable"),
                                fieldWithPath("roadNameNumber").type(JsonFieldType.STRING).description("roadNameNumber"),
                                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("profileImg"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("introduction"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("createdAt"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("updatedAt")
                        )
                ));
    }

    @Test
    @DisplayName("헤어샵 정보를 수정 할 수 있다.")
    void MODIFY_HAIRSHOP_TEST() throws Exception {
        // TODO : 유저 기능 작성 완료 후 리팩토링
        HairshopDto hairshopDto = HairshopDto.builder()
                .userId(1L)
                .build();
        this.mockMvc.perform(patch("/api/v1/hairshop")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hairshopDto)))
                .andExpect(status().isNoContent())
                .andDo(document("modify-hairshop",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("phoneNumber"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("startTime"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("endTime"),
                                fieldWithPath("closedDay").type(JsonFieldType.STRING).description("closedDay"),
                                fieldWithPath("reservationRange").type(JsonFieldType.STRING).description("reservationRange"),
                                fieldWithPath("reservationStartTime").type(JsonFieldType.STRING).description("reservationStartTime"),
                                fieldWithPath("reservationEndTime").type(JsonFieldType.STRING).description("reservationEndTime"),
                                fieldWithPath("sameDayAvailable").type(JsonFieldType.BOOLEAN).description("sameDayAvailable"),
                                fieldWithPath("roadNameNumber").type(JsonFieldType.STRING).description("roadNameNumber"),
                                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("profileImg"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("introduction"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId")
                        )
                ));
    }

    @Test
    @DisplayName("게시물을 삭제 할 수 있다.")
    void REMOVE_USER_TEST() throws Exception {
        this.mockMvc.perform(delete("/api/v1/hairshop/{id}", hairshopResponse.getId()))
                .andExpect(status().isNoContent())
                .andDo(document("remove-hairshop"));
    }
}