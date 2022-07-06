package com.prgms.kokoahairshop.reservation.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Gender;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.entity.Type;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
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
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootTest
@Transactional
public class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @PersistenceContext
    EntityManager em;

    User manager;

    User user;

    Hairshop hairshop;

    Designer designer;

    Menu menu;

    Reservation reservation;

    @BeforeEach
    void setup() {
        // given
        manager = User.builder()
            .email("example1@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        em.persist(manager);

        user = User.builder()
            .email("example2@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        em.persist(user);

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
            .user(manager)
            .build();
        em.persist(hairshop);

        designer = Designer.builder()
            .name("디자이너")
            .image("디자이너_이미지_URL")
            .introduction("안녕하세요.")
            .position(Position.DESIGNER)
            .hairshop(hairshop)
            .build();
        em.persist(designer);

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
        em.persist(menu);

        em.clear();

        reservationRepository.deleteAll();

        reservation = Reservation.builder()
            .name("예약자")
            .date(LocalDate.now())
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
        reservationRepository.save(reservation);
    }

    @Test
    void 예약을_생성할_수_있다() {
        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations.size(), is(1));
    }

    @Test
    void 예약을_조회할_수_있다() {
        // when
        Reservation receivedReservation = reservationRepository.findById(reservation.getId()).get();

        // then
        assertThat(receivedReservation.getName(), is("예약자"));
    }

    @Test
    void 예약을_수정할_수_있다() {
        // when
        reservation.changeStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
        Reservation receivedReservation = reservationRepository.findById(reservation.getId()).get();

        // then
        assertThat(receivedReservation.getStatus(), is(ReservationStatus.CANCELED));
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // when
        reservationRepository.deleteById(reservation.getId());
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations.size(), is(0));
    }

    @Test
    void 날짜_시간_디자이너_아이디로_예약이_존재하는지_확인할_수_있다() {
        // when
        boolean exists = reservationRepository.existsByDateAndTimeAndDesignerId(LocalDate.now(),
            "12:00",
            designer.getId());

        // then
        assertThat(exists, is(true));
    }

    @Test
    @DisplayName("사용자의 예약 리스트를 검색할 수 있다.")
    void findReservationsByUserIdTest() {
        // When
        List<Reservation> reservations = reservationRepository.findReservationsByUserId(
            user.getId());

        // Then
        assertThat(reservations.size(), is(1));
    }

    @Test
    @DisplayName("헤어샵의 예약 리스트를 검색할 수 있다.")
    void findReservationsByHairshopIdTest() {
        // When
        List<Reservation> reservations = reservationRepository.findReservationsByHairshopId(
            hairshop.getId());

        // Then
        assertThat(reservations.size(), is(1));
    }
}