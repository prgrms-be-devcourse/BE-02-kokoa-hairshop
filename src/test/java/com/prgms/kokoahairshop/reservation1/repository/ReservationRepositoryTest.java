package com.prgms.kokoahairshop.reservation1.repository;

import static org.assertj.core.api.Assertions.*;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Gender;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.entity.Type;
import com.prgms.kokoahairshop.reservation1.entity.Reservation;
import com.prgms.kokoahairshop.reservation1.entity.ReservationStatus;
import com.prgms.kokoahairshop.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @PersistenceContext
    EntityManager em;

    User user;
    Hairshop hairshop;

    @BeforeEach
    void beforeEach() {
        // Given
        user = User.builder()
            .email("hhhh@gmail.com")
            .password("123456")
            .auth("admin")
            .build();
        hairshop =  Hairshop.builder()
            .name("데브헤어")
            .phoneNumber("010-1234-5678")
            .startTime("11:00")
            .endTime("20:00")
            .closedDay("7")
            .reservationRange("1")
            .reservationStartTime("11:00")
            .reservationEndTime("19:30")
            .sameDayAvailable(true)
            .roadNameNumber("대구 중구 동성로2가 141-9 2층3층")
            .profileImg("이미지 URL")
            .introduction("안녕하세요.")
            .userId(1L)
            .build();
        Designer designer = Designer.builder()
            .name("designer")
            .image("/image")
            .introduction("소개글")
            .position(Position.DESIGNER)
            .hairshop(hairshop)
            .build();
        Menu menu = Menu.builder()
            .name("커트")
            .price(20000)
            .discount(1000)
            .gender(Gender.남)
            .type(Type.커트)
            .image("/image")
            .exposedTime(1)
            .hairshop(hairshop)
            .build();
        em.persist(user);
        em.persist(hairshop);
        em.persist(designer);
        em.persist(menu);

        Reservation reservation = Reservation.builder()
            .name("CHOI")
            .phoneNumber("01000000000")
            .date(LocalDate.now())
            .time("11:00")
            .status(ReservationStatus.RESERVED)
            .request("요청사항")
            .paymentAmount(15000)
            .user(user)
            .designer(designer)
            .hairshop(hairshop)
            .menu(menu)
            .build();
        reservationRepository.save(reservation);
    }

    @Test
    @DisplayName("사용자의 예약 리스트를 검색할 수 있다.")
    @Rollback(value = false)
    void findReservationsByUserIdTest() {
        // When
        em.clear();

        List<Reservation> reservations = reservationRepository.findReservationsByUserId(user.getId());

        // Then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("헤어샵의 예약 리스트를 검색할 수 있다.")
    void findReservationsByHairshopIdTest() {
        // When
        em.clear();

        List<Reservation> reservations = reservationRepository.findReservationsByHairshopId(hairshop.getId());

        // Then
        assertThat(reservations.size()).isEqualTo(1);
    }


}