package com.prgms.kokoahairshop.reservation.controller;

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
import com.prgms.kokoahairshop.reservation.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDtoDynamic;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDtoStatic;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation.repository.ReservationRepository;
import com.prgms.kokoahairshop.reservation.repository.ReservationTimeRepository;
import com.prgms.kokoahairshop.reservation.service.ReservationService;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationControllerTest {

    @Autowired
    ReservationService reservationService;

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
    ReservationRepository reservationRepository;

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    User manager;

    User user;

    Hairshop hairshop;

    Designer designer;

    Menu menu;

    Reservation reservation1;

    Reservation reservation2;

    Reservation reservation3;

    @BeforeAll
    void setup() {
        // given
        manager = User.builder()
            .email("example1@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        userRepository.save(manager);

        user = User.builder()
            .email("example2@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        userRepository.save(user);

        hairshop = Hairshop.builder()
            .name("?????????")
            .phoneNumber("010-1234-1234")
            .startTime("11:00")
            .endTime("20:00")
            .closedDay("???")
            .reservationRange("1")
            .reservationStartTime("11:00")
            .reservationEndTime("19:30")
            .sameDayAvailable(true)
            .roadNameNumber("?????? ?????? ?????????2??? 141-9 2???3???")
            .profileImg("?????????_?????????_URL")
            .introduction("?????? ?????? ????????? ????????? ???????????? ???????????? ????????????????????? :)")
            .user(manager)
            .build();
        hairshopRepository.save(hairshop);

        designer = Designer.builder()
            .name("????????????")
            .image("????????????_?????????_URL")
            .introduction("???????????????.")
            .position(Position.DESIGNER)
            .hairshop(hairshop)
            .build();
        designerRepository.save(designer);

        menu = Menu.builder()
            .name("?????? ??????")
            .type(Type.haircut)
            .price(20000)
            .gender(Gender.man)
            .exposedTime(30)
            .discount(0)
            .image("??????_?????????_URL")
            .hairshop(hairshop)
            .build();
        menuRepository.save(menu);

        reservation1 = Reservation.builder()
            .name("?????????")
            .date(LocalDate.now().minusDays(1))
            .time("12:00")
            .paymentAmount(20000)
            .status(ReservationStatus.RESERVED)
            .phoneNumber("010-1234-5678")
            .request("????????? ???????????????.")
            .user(user)
            .hairshop(hairshop)
            .designer(designer)
            .menu(menu)
            .build();
        reservationRepository.save(reservation1);

        reservation2 = Reservation.builder()
            .name("?????????")
            .date(LocalDate.now().plusDays(1))
            .time("12:30")
            .paymentAmount(20000)
            .status(ReservationStatus.RESERVED)
            .phoneNumber("010-1234-5678")
            .request("????????? ???????????????.")
            .user(user)
            .hairshop(hairshop)
            .designer(designer)
            .menu(menu)
            .build();
        reservationRepository.save(reservation2);

        reservation3 = Reservation.builder()
            .name("?????????")
            .date(LocalDate.now().plusDays(1))
            .time("13:00")
            .paymentAmount(20000)
            .status(ReservationStatus.RESERVED)
            .phoneNumber("010-1234-5678")
            .request("????????? ???????????????.")
            .user(user)
            .hairshop(hairshop)
            .designer(designer)
            .menu(menu)
            .build();
        reservationRepository.save(reservation3);

        // ReservationTime ??????
        LocalDate date = LocalDate.now();
        StringTokenizer st;
        String reservationStartTime = designer.getHairshop().getReservationStartTime();
        String reservationEndTime = designer.getHairshop().getReservationEndTime();
        st = new StringTokenizer(reservationStartTime, ":");
        int startHour = Integer.parseInt(st.nextToken());
        int startMinute = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(reservationEndTime, ":");
        int endHour = Integer.parseInt(st.nextToken());
        int endMinute = Integer.parseInt(st.nextToken());

        while (startHour <= endHour) {
            if (startHour == endHour && startMinute > endMinute) {
                break;
            }
            String str;
            if (startMinute == 0) {
                str = "00";
            } else {
                str = "30";
            }
            ReservationTime reservationTime = ReservationTime.builder()
                .date(date)
                .time(startHour + ":" + str)
                .reserved(false)
                .build();
            reservationTime.setDesigner(designer);
            reservationTime.setHairshop(designer.getHairshop());
            reservationTimeRepository.save(reservationTime);

            startMinute += 30;
            if (startMinute >= 60) {
                startHour += 1;
                startMinute = 0;
            }
        }
    }

    @AfterAll
    void tearDown() {
        reservationTimeRepository.deleteAll();
        reservationRepository.deleteAll();
        menuRepository.deleteAll();
        designerRepository.deleteAll();
        hairshopRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("????????? ????????? ??? ??????")
    @WithUserDetails(value = "example2@naver.com")
    void CREATE_RESERVATION_DYNAMIC_TEST() throws Exception {
        CreateReservationRequestDto requestDto = CreateReservationRequestDto.builder()
            .name("?????????")
            .phoneNumber("010-1234-5678")
            .date(LocalDate.now())
            .time("11:00") // 11????????? ?????? ????????? ?????? ??????
            .request("????????? ???????????????.")
            .paymentAmount(20000)
            .userId(user.getId())
            .hairshopId(hairshop.getId())
            .designerId(designer.getId())
            .menuId(menu.getId())
            .build();

        mockMvc.perform(post("/v2/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("create-reservation-dynamic",
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
    @DisplayName("?????? ???????????? 400?????? ??????")
    @WithUserDetails(value = "example2@naver.com")
    void RETURN_400_ERROR_IF_EXIST_RESERVATION() throws Exception {
        CreateReservationRequestDto requestDto = CreateReservationRequestDto.builder()
            .name("?????????")
            .phoneNumber("010-1234-5678")
            .date(LocalDate.now().minusDays(1))
            .time("12:00") // 12????????? ?????? ????????? ???????????? ??????
            .request("????????? ???????????????.")
            .paymentAmount(20000)
            .userId(user.getId())
            .hairshopId(hairshop.getId())
            .designerId(designer.getId())
            .menuId(menu.getId())
            .build();

        mockMvc.perform(post("/v2/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("??????_???????????????_?????????_???_??????")
    @WithUserDetails(value = "example2@naver.com")
    void GET_RESERVATIONTIMES_DYNAMIC_TEST() throws Exception {
        ReservationTimeRequestDtoDynamic requestDto = ReservationTimeRequestDtoDynamic.builder()
            .date(LocalDate.now())
            .reservationStartTime("10:00")
            .reservationEndTime("20:00")
            .build();

        mockMvc.perform(
                get("/v2/reservations/reservation-times/hairshops/{hairshopId}", hairshop.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print()) // ?????? ????????? "12:00" ???????????? ????????? ??? ??????
            .andDo(document("get-reservation-times-dynamic",
                requestFields(
                    fieldWithPath("date").type(JsonFieldType.STRING).description("date"),
                    fieldWithPath("reservationStartTime").type(JsonFieldType.STRING)
                        .description("reservationStartTime"),
                    fieldWithPath("reservationEndTime").type(JsonFieldType.STRING)
                        .description("reservationEndTime")
                ),
                responseFields(
                    fieldWithPath("[].designerId").type(JsonFieldType.NUMBER)
                        .description("[].designerId"),
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
    @DisplayName("???????????? ????????? ????????? ??? ??????")
    @WithUserDetails(value = "example2@naver.com")
    void CANCEL_RESERVATION_BY_USER_DYNAMIC_TEST() throws Exception {
        mockMvc.perform(
                patch("/v2/reservations/{reservationId}/user", reservation2.getId()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("cancel-reservation-by-user-dynamic"));
    }

    @Test
    @Order(5)
    @DisplayName("???????????? ????????? ????????? ??? ??????")
    @WithUserDetails(value = "example1@naver.com")
    void CANCEL_RESERVATION_BY_HAIRSHOP_DYNAMIC_TEST() throws Exception {
        mockMvc.perform(
                patch("/v2/reservations/{reservationId}/hairshop", reservation3.getId()))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("cancel-reservation-by-hairshop-dynamic"));
    }

    @Test
    @Order(6)
    @DisplayName("?????? ???????????? ????????? ????????? 400????????? ????????????")
    @WithUserDetails(value = "example2@naver.com")
    void RETURN_400_ERROR_IF_EXPIRE_TIME_TEST() throws Exception {
        mockMvc.perform(
                patch("/v2/reservations/{reservationId}/user", reservation1.getId()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("?????? ????????? ??? ??????.")
    @WithUserDetails(value = "example2@naver.com")
    void CREATE_RESERVATION_STATIC_TEST() throws Exception {
        log.info("{}", user.getId());
        CreateReservationRequestDto requestDto = CreateReservationRequestDto.builder()
            .name("?????????")
            .phoneNumber("010-1234-5678")
            .date(LocalDate.now())
            .time("14:00")
            .request("????????? ???????????????.")
            .paymentAmount(20000)
            .userId(user.getId())
            .hairshopId(hairshop.getId())
            .designerId(designer.getId())
            .menuId(menu.getId())
            .build();

        mockMvc.perform(post("/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("create-reservation-static",
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
    @DisplayName("?????? ????????? ????????? ??????????????? ?????? 400 ????????? ????????????.")
    @WithUserDetails(value = "example2@naver.com")
    void RETURN_400_ERROR_IF_ALREADY_RESERVED_TEST() throws Exception {
        log.info("{}", user.getId());
        CreateReservationRequestDto requestDto = CreateReservationRequestDto.builder()
            .name("?????????")
            .phoneNumber("010-1234-5678")
            .date(LocalDate.now())
            .time("11:00")
            .request("????????? ???????????????.")
            .paymentAmount(20000)
            .userId(user.getId() + 1) // ?????? user_id
            .hairshopId(hairshop.getId())
            .designerId(designer.getId())
            .menuId(menu.getId())
            .build();

        mockMvc.perform(post("/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ??????")
    @WithUserDetails(value = "example2@naver.com")
    void GET_RESERVATIONTIMES_STATIC_TEST() throws Exception {
        ReservationTimeRequestDtoStatic requestDto = new ReservationTimeRequestDtoStatic(
            LocalDate.now());

        mockMvc.perform(
                get("/v1/reservations/reservation-times/hairshops/{hairshopId}",
                    hairshop.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("get-reservation-times-static",
                requestFields(
                    fieldWithPath("date").type(JsonFieldType.STRING).description("date")
                ),
                responseFields(
                    fieldWithPath("[].designerId").type(JsonFieldType.NUMBER)
                        .description("[].designerId"),
                    fieldWithPath("[].designerPosition").type(JsonFieldType.STRING)
                        .description("[].designerPosition"),
                    fieldWithPath("[].designerName").type(JsonFieldType.STRING)
                        .description("[].designerName"),
                    fieldWithPath("[].designerImage").type(JsonFieldType.STRING)
                        .description("[].designerImage"),
                    fieldWithPath("[].designerInstruction").type(JsonFieldType.STRING)
                        .description("[].designerInstruction"),
                    fieldWithPath("[].reservationTimes[]").type(JsonFieldType.ARRAY)
                        .description("[].reservationTimes")
                )
            ));
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ??????")
    @WithUserDetails(value = "example2@naver.com")
    void GET_RESERVATIONS_BY_USER_TEST() throws Exception {
        mockMvc.perform(get("/reservations/user", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("get-reservations-by-user",
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                            .description("[].id"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING)
                            .description("[].name"),
                        fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING)
                            .description("[].phoneNumber"),
                        fieldWithPath("[].date").type(JsonFieldType.STRING)
                            .description("[].date"),
                        fieldWithPath("[].time").type(JsonFieldType.STRING)
                            .description("[].time"),
                        fieldWithPath("[].status").type(JsonFieldType.STRING)
                            .description("[].status"),
                        fieldWithPath("[].request").type(JsonFieldType.STRING)
                            .description("[].request"),
                        fieldWithPath("[].paymentAmount").type(JsonFieldType.NUMBER)
                            .description("[].paymentAmount"),
                        fieldWithPath("[].hairshopId").type(JsonFieldType.NUMBER)
                            .description("[].hairshopId"),
                        fieldWithPath("[].hairshopName").type(JsonFieldType.STRING)
                            .description("[].hairshopName"),
                        fieldWithPath("[].menuId").type(JsonFieldType.NUMBER)
                            .description("[].menuId"),
                        fieldWithPath("[].menuName").type(JsonFieldType.STRING)
                            .description("[].menuName"),
                        fieldWithPath("[].designerId").type(JsonFieldType.NUMBER)
                            .description("[].designerId"),
                        fieldWithPath("[].designerPosition").type(JsonFieldType.STRING)
                            .description("[].designerPosition"),
                        fieldWithPath("[].designerName").type(JsonFieldType.STRING)
                            .description("[].designerName")
                    )
                ));
    }


    @Test
    @DisplayName("???????????? ?????? ????????? ??????")
    @WithUserDetails(value = "example2@naver.com")
    void GET_RESERVATIONS_BY_HAIRSHOP_TEST() throws Exception {
        mockMvc.perform(
                get("/reservations/hairshops/{hairshopId}", hairshop.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("get-reservations-by-hairshop",
                responseFields(
                    fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                        .description("[].id"),
                    fieldWithPath("[].name").type(JsonFieldType.STRING)
                        .description("[].name"),
                    fieldWithPath("[].phoneNumber").type(JsonFieldType.STRING)
                        .description("[].phoneNumber"),
                    fieldWithPath("[].date").type(JsonFieldType.STRING)
                        .description("[].date"),
                    fieldWithPath("[].time").type(JsonFieldType.STRING)
                        .description("[].time"),
                    fieldWithPath("[].status").type(JsonFieldType.STRING)
                        .description("[].status"),
                    fieldWithPath("[].request").type(JsonFieldType.STRING)
                        .description("[].request"),
                    fieldWithPath("[].paymentAmount").type(JsonFieldType.NUMBER)
                        .description("[].paymentAmount"),
                    fieldWithPath("[].hairshopId").type(JsonFieldType.NUMBER)
                        .description("[].hairshopId"),
                    fieldWithPath("[].hairshopName").type(JsonFieldType.STRING)
                        .description("[].hairshopName"),
                    fieldWithPath("[].menuId").type(JsonFieldType.NUMBER)
                        .description("[].menuId"),
                    fieldWithPath("[].menuName").type(JsonFieldType.STRING)
                        .description("[].menuName"),
                    fieldWithPath("[].designerId").type(JsonFieldType.NUMBER)
                        .description("[].designerId"),
                    fieldWithPath("[].designerPosition").type(JsonFieldType.STRING)
                        .description("[].designerPosition"),
                    fieldWithPath("[].designerName").type(JsonFieldType.STRING)
                        .description("[].designerName")
                )
            ));
    }

}