package com.prgms.kokoahairshop.reservation1.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.entity.Type;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation1.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservation;
import com.prgms.kokoahairshop.reservation1.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation1.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation1.repository.ReservationRepository;
import com.prgms.kokoahairshop.reservation1.repository.ReservationTimeRepository;
import com.prgms.kokoahairshop.reservation1.service.ReservationService;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.StringTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class ReservationControllerTest {

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

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    User hairshopManager;

    User user;

    Hairshop hairshop;

    Designer designer;

    Menu menu;

    Reservation reservation; // 예약 취소 가능시간 지난 예약

    Reservation reservation2; // 예약 취소 가능시간 안지난 예약

    @BeforeEach
    void beforeEach() {
        user = User.builder()
            .email("example2@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        userRepository.save(user);

        hairshopManager = User.builder()
            .email("example1@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        userRepository.save(hairshopManager);

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
            .userId(user.getId())
            .introduction("시간 여유 충분히 가지고 여유롭게 와주시면 감사하겠습니다 :)")
            .build();
        hairshopRepository.save(hairshop);

        designer = Designer.builder()
            .name("디자이너")
            .image("디자이너_이미지_URL")
            .introduction("안녕하세요.")
            .position(Position.디자이너)
            .hairshop(hairshop)
            .build();
        designerRepository.save(designer);

        menu = Menu.builder()
            .name("기본 커트")
            .type(Type.커트)
            .price(20000)
            .gender("남")
            .exposed_time(30)
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
            ReservationTime reservationTime = ReservationTime.builder()
                .date(date)
                .time(startHour + ":" + startMinute)
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

    @Test
    @DisplayName("예약 가능한 시간 조회")
    @WithMockUser(username = "example@gmail.com", roles = "USER")
    void reservationTimeListTest() throws Exception {
        ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto(LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/reservation-time/hairshops/{id}", hairshop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("사용자의 예약 리스트 조회")
    @WithMockUser(username = "example@gmail.com", roles = "USER")
    void reservationListByUserTest() throws Exception {
        //Todo : Mock jwt를 만들어서 test
        mockMvc.perform(MockMvcRequestBuilders.get(  "/reservations/user", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("헤어샵의 예약 리스트 조회")
    @WithMockUser(username = "example@gmail.com", roles = "USER")
    void reservationListByHairshopTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get( "/reservations/hairshops/{id}", hairshop.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }
}