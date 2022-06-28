package com.prgms.kokoahairshop.reservation2.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.entity.Type;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import com.prgms.kokoahairshop.reservation2.entity.ReservationStatus;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ReservationRepositoryTest {

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

    Reservation reservation;

    @BeforeAll
    void setup() {
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
    }

    @BeforeEach
    void setupEach() {
        repository.deleteAll();

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
        repository.save(reservation);
    }

    @Test
    void 날짜_시간_디자이너_아이디로_예약이_존재하는지_확인할_수_있다() {
        boolean exists = repository.existsByDateAndTimeAndDesignerId(LocalDate.now(), "12:00", designer.getId());

        assertThat(exists, is(true));
    }

    @Test
    void 예약을_생성할_수_있다() {
        List<Reservation> reservations = repository.findAll();

        assertThat(reservations.size(), is(1));
    }

    @Test
    void 예약을_조회할_수_있다() {
        Reservation receivedReservation = repository.findById(reservation.getId()).get();

        assertThat(receivedReservation.getName(), is("예약자"));
    }

    @Test
    void 예약을_수정할_수_있다() {
        reservation.changeStatus(ReservationStatus.CANCELED);
        repository.save(reservation);
        Reservation receivedReservation = repository.findById(reservation.getId()).get();

        assertThat(receivedReservation.getStatus(), is(ReservationStatus.CANCELED));
    }

    @Test
    void 예약을_삭제할_수_있다() {
        repository.deleteById(reservation.getId());
        List<Reservation> reservations = repository.findAll();

        assertThat(reservations.size(), is(0));
    }
}