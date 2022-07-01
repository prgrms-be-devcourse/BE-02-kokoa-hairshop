package com.prgms.kokoahairshop.reservation2.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Gender;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.entity.Type;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation2.dto.ReservationRequestDto;
import com.prgms.kokoahairshop.reservation2.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import com.prgms.kokoahairshop.reservation2.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation2.repository.ReservationRepository;
import com.prgms.kokoahairshop.reservation2.service.ReservationService;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationsController1Test {

    @Autowired
    ReservationService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HairshopRepository hairshopRepository;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ReservationRepository repository;

    User hairshopManager;

    User user;

    Hairshop hairshop;

    Designer designer;

    Menu menu;

    Reservation reservation; // 예약 취소 가능시간 지난 예약

    Reservation reservation2; // 예약 취소 가능시간 안지난 예약

    @BeforeAll
    void setup() {
        // given
        hairshopManager = User.builder()
            .email("example1@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        userRepository.save(hairshopManager);

        user = User.builder()
            .email("example2@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        userRepository.save(user);

        hairshop = Hairshop.builder()
            .name("헤어샵")
            .phoneNumber("010-1234-1234")
            .startTime("11:00")
            .endTime("20:00")
            .closedDay("화")
            .reservationRange("1")
            .reservationStartTime("11:00")
            .reservationEndTime("19:30")
            .sameDayAvailable(true)
            .roadNameNumber("대구 중구 동성로2가 141-9 2층3층")
            .profileImg("헤어샵_이미지_URL")
            .introduction("시간 여유 충분히 가지고 여유롭게 와주시면 감사하겠습니다 :)")
            .user(hairshopManager)
            .build();
        hairshopRepository.save(hairshop);

        designer = Designer.builder()
            .name("디자이너")
            .image("디자이너_이미지_URL")
            .introduction("안녕하세요.")
            .position(Position.DESIGNER)
            .hairshop(hairshop)
            .build();
        designerRepository.save(designer);

        menu = Menu.builder()
            .name("기본 커트")
            .type(Type.haircut)
            .price(20000)
            .gender(Gender.man)
            .exposedTime(30)
            .discount(0)
            .image("커트_이미지_URL")
            .hairshop(hairshop)
            .build();
        menuRepository.save(menu);

        reservation = Reservation.builder()
            .name("예약자")
            .date(LocalDate.now().minusDays(1))
            .time("12:00")
            .paymentAmount(20000)
            .status(ReservationStatus.RESERVED)
            .phoneNumber("010-1234-5678")
            .request("예쁘게 잘라주세요.")
            .user(user)
            .hairshop(hairshop)
            .designer(designer)
            .menu(menu)
            .build();
        repository.save(reservation);

        reservation2 = Reservation.builder()
            .name("예약자")
            .date(LocalDate.now().plusDays(1))
            .time("12:00")
            .paymentAmount(20000)
            .status(ReservationStatus.RESERVED)
            .phoneNumber("010-1234-5678")
            .request("예쁘게 잘라주세요.")
            .user(user)
            .hairshop(hairshop)
            .designer(designer)
            .menu(menu)
            .build();
        repository.save(reservation2);
    }

    @Test
    @Order(1)
    @WithMockUser(username = "example@gmail.com", roles = "USER")
    void 예약을_생성할_수_있다() throws Exception {
        ReservationRequestDto requestDto = ReservationRequestDto.builder()
            .name("예약자")
            .phoneNumber("010-1234-5678")
            .date(LocalDate.now())
            .time("11:00") // 11시에는 예약 없어서 생성 성공
            .request("예쁘게 잘라주세요.")
            .paymentAmount(20000)
            .userId(user.getId())
            .hairshopId(hairshop.getId())
            .designerId(designer.getId())
            .menuId(menu.getId())
            .build();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("create-reservation",
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("phoneNumber"),
                    fieldWithPath("date").type(JsonFieldType.STRING).description("date"),
                    fieldWithPath("time").type(JsonFieldType.STRING).description("time"),
                    fieldWithPath("request").type(JsonFieldType.STRING).description("request"),
                    fieldWithPath("paymentAmount").type(JsonFieldType.NUMBER)
                        .description("paymentAmount"),
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("userId"),
                    fieldWithPath("hairshopId").type(JsonFieldType.NUMBER)
                        .description("hairshopId"),
                    fieldWithPath("designerId").type(JsonFieldType.NUMBER)
                        .description("designerId"),
                    fieldWithPath("menuId").type(JsonFieldType.NUMBER).description("menuId")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("id")
                )
            ));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "example@gmail.com", roles = "USER")
    void 예약_존재하면_400에러_발생() throws Exception {
        ReservationRequestDto requestDto = ReservationRequestDto.builder()
            .name("예약자")
            .phoneNumber("010-1234-5678")
            .date(LocalDate.now().minusDays(1))
            .time("12:00") // 12시에는 이미 예약이 있으므로 실패
            .request("예쁘게 잘라주세요.")
            .paymentAmount(20000)
            .userId(user.getId())
            .hairshopId(hairshop.getId())
            .designerId(designer.getId())
            .menuId(menu.getId())
            .build();

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "example@gmail.com", roles = "USER")
    void 예약_가능시간을_조회할_수_있다() throws Exception {
        ReservationTimeRequestDto requestDto = ReservationTimeRequestDto.builder()
            .date(LocalDate.now())
            .reservationStartTime("10:00")
            .reservationEndTime("20:00")
            .build();

        mockMvc.perform(
                get("/reservations/reservation-times/hairshops/{hairshopId}", hairshop.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print()) // 이미 예약된 "12:00" 제외하고 출력된 것 확인
            .andDo(document("get-reservation-times",
                requestFields(
                    fieldWithPath("date").type(JsonFieldType.STRING).description("date"),
                    fieldWithPath("reservationStartTime").type(JsonFieldType.STRING)
                        .description("reservationStartTime"),
                    fieldWithPath("reservationEndTime").type(JsonFieldType.STRING)
                        .description("reservationEndTime")
                ),
                responseFields(
                    fieldWithPath("[].designerPosition").type(JsonFieldType.STRING)
                        .description("[].designerPosition"),
                    fieldWithPath("[].designerName").type(JsonFieldType.STRING)
                        .description("[].designerName"),
                    fieldWithPath("[].designerImage").type(JsonFieldType.STRING)
                        .description("[].designerImage"),
                    fieldWithPath("[].designerInstruction").type(JsonFieldType.STRING)
                        .description("[].designerInstruction"),
                    fieldWithPath("[].reservationTimes").type(JsonFieldType.ARRAY)
                        .description("[].reservationTimes")
                )
            ));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "example@gmail.com", roles = "USER")
    void 사용자는_예약을_취소할_수_있다() throws Exception {
        mockMvc.perform(
                patch("/reservations/{reservationId}/user", reservation2.getId()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("cancel-reservation"));

        // 헤어샵도 동일하기 때문에 패스
    }

    @Test
    @Order(5)
    @WithMockUser(username = "example@gmail.com", roles = "USER")
    void 예약_취소가눙_시간이_지나면_400에러를_반환한다() throws Exception {
        mockMvc.perform(
                patch("/reservations/{reservationId}/user", reservation.getId()))
            .andExpect(status().isBadRequest());
    }
}