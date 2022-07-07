package com.prgms.kokoahairshop.hairshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.kokoahairshop.hairshop.dto.CreateHairshopRequest;
import com.prgms.kokoahairshop.hairshop.dto.HairshopConverter;
import com.prgms.kokoahairshop.hairshop.dto.ModifyHairshopRequest;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.hairshop.service.HairshopService;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureRestDocs
@DisplayName("헤어샵 CRUD API 테스트")
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HairshopControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HairshopService hairshopService;

    @Autowired
    private HairshopRepository hairshopRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HairshopConverter hairshopConverter;

    Hairshop hairshop;
    CreateHairshopRequest createHairshopRequest;
    User user;

    @BeforeAll
    void setup() {
        user = User.builder()
                .email("example2@naver.com")
                .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
                .auth("USER")
                .build();
        userRepository.save(user);
        createHairshopRequest = CreateHairshopRequest.builder()
                .name("코스헤어")
                .phoneNumber("010-1234-1234")
                .startTime("11:00")
                .endTime("20:00")
                .closedDay("화")
                .reservationRange("1")
                .reservationStartTime("11:00")
                .reservationEndTime("19:30")
                .sameDayAvailable(true)
                .roadNameNumber("대구 중구 동성로2가 143-9 2층")
                .profileImg("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .introduction("예약 전 DM으로 먼저 문의해주세요 :)")
                .userId(user.getId())
                .build();
        hairshop = hairshopConverter.convertToHairshop(createHairshopRequest);
        hairshop = hairshopRepository.save(hairshop);
    }

    @AfterAll
    void tearDown() {
        hairshopRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("헤어샵 등록 테스트")
    void HAIRSHOP_INSERT_TEST() throws Exception {
        createHairshopRequest = CreateHairshopRequest.builder()
                .name("코스헤어")
                .phoneNumber("010-1234-1234")
                .startTime("11:00")
                .endTime("20:00")
                .closedDay("화")
                .reservationRange("1")
                .reservationStartTime("11:00")
                .reservationEndTime("19:30")
                .sameDayAvailable(true)
                .roadNameNumber("대구 중구 동성로2가 143-9 2층")
                .profileImg("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .introduction("예약 전 DM으로 먼저 문의해주세요 :)")
                .userId(user.getId())
                .build();
        this.mockMvc.perform(post("/api/v1/hairshops")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createHairshopRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
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
                                )
                        ));
    }

    @Test
    @Order(2)
    @DisplayName("전체 헤어샵 조회 테스트")
    void GET_HAIRSHOP_LIST_TEST() throws Exception {
        this.mockMvc.perform(get("/api/v1/hairshops")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
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
    @Order(3)
    @DisplayName("헤어샵 아이디로 헤어샵 조회 테스트")
    void GET_HAIRSHOP_BY_ID_TEST() throws Exception {
        this.mockMvc.perform(get("/api/v1/hairshops/{id}", hairshop.getId())
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json")))
                .andExpect(jsonPath("$.id").value(hairshop.getId()))
                .andExpect(jsonPath("$.name").value(hairshop.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(hairshop.getPhoneNumber()))
                .andExpect(jsonPath("$.startTime").value(hairshop.getStartTime()))
                .andExpect(jsonPath("$.endTime").value(hairshop.getEndTime()))
                .andExpect(jsonPath("$.closedDay").value(hairshop.getClosedDay()))
                .andExpect(jsonPath("$.reservationRange").value(hairshop.getReservationRange()))
                .andExpect(jsonPath("$.reservationStartTime").value(hairshop.getReservationStartTime()))
                .andExpect(jsonPath("$.reservationEndTime").value(hairshop.getReservationEndTime()))
                .andExpect(jsonPath("$.sameDayAvailable").value(hairshop.getSameDayAvailable()))
                .andExpect(jsonPath("$.roadNameNumber").value(hairshop.getRoadNameNumber()))
                .andExpect(jsonPath("$.profileImg").value(hairshop.getProfileImg()))
                .andExpect(jsonPath("$.introduction").value(hairshop.getIntroduction()))
                .andDo(document("getById-hairshop",
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
    @Order(4)
    @DisplayName("해당 아이디의 헤어샵이 없을 경우 테스트")
    void GET_HAIRSHOP_BY_ID_NOT_FOUND_TEST() throws Exception {
        this.mockMvc.perform(get("/api/v1/hairshops/{id}", 999L)
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("getById-notFound-hairshop"));
    }

    @Test
    @Order(5)
    @DisplayName("헤어샵 정보를 수정 할 수 있다.")
    void MODIFY_HAIRSHOP_TEST() throws Exception {
        ModifyHairshopRequest modifyHairshopRequest = ModifyHairshopRequest.builder()
                .id(hairshop.getId())
                .name(hairshop.getName())
                .phoneNumber(hairshop.getPhoneNumber())
                .startTime(hairshop.getStartTime())
                .endTime(hairshop.getEndTime())
                .closedDay(hairshop.getClosedDay())
                .reservationRange(hairshop.getReservationRange())
                .reservationStartTime(hairshop.getReservationStartTime())
                .reservationEndTime(hairshop.getEndTime())
                .sameDayAvailable(hairshop.getSameDayAvailable())
                .roadNameNumber(hairshop.getRoadNameNumber())
                .profileImg(hairshop.getProfileImg())
                .introduction(hairshop.getIntroduction())
                .userId(createHairshopRequest.getUserId())
                .build();
        this.mockMvc.perform(patch("/api/v1/hairshops")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyHairshopRequest)))
                .andExpect(status().isNoContent())
                .andDo(document("modify-hairshop",
                        requestFields(
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
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId")
                        )
                ));
    }

    @Test
    @Order(6)
    @DisplayName("수정하려는 헤어샵이 없을 경우 테스트")
    void MODIFY_HAIRSHOP_NOT_FOUND_TEST() throws Exception {
        ModifyHairshopRequest modifyHairshopRequest = ModifyHairshopRequest.builder()
                .id(999L)
                .name("코스헤어")
                .phoneNumber("010-1234-1234")
                .startTime("11:00")
                .endTime("20:00")
                .closedDay("화")
                .reservationRange("1")
                .reservationStartTime("11:00")
                .reservationEndTime("19:30")
                .sameDayAvailable(true)
                .roadNameNumber("대구 중구 동성로2가 143-9 2층")
                .profileImg("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .introduction("예약 전 DM으로 먼저 문의해주세요 :)")
                .userId(user.getId())
                .build();
        this.mockMvc.perform(patch("/api/v1/hairshops")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyHairshopRequest)))
                .andExpect(status().isNotFound())
                .andDo(document("modify-notFound-hairshop"));
    }

    @Test
    @Order(7)
    @DisplayName("헤어샵을 삭제 할 수 있다.")
    void REMOVE_USER_TEST() throws Exception {
        this.mockMvc.perform(delete("/api/v1/hairshops/{id}", hairshop.getId()))
                .andExpect(status().isNoContent())
                .andDo(document("remove-hairshop"));
    }
}