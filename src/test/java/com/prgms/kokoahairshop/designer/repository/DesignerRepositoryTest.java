package com.prgms.kokoahairshop.designer.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.entity.Type;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation.repository.ReservationRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DesignerRepositoryTest {

    @Autowired
    DesignerRepository repository;

    @Autowired
    HairshopRepository hairshopRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    void 헤어샵_아이디와_날짜로_디자이너_리스트를_가져올_수_있다() {
        // given
        Hairshop hairshop = Hairshop.builder()
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
        hairshopRepository.save(hairshop);

        Designer designer1 = Designer.builder()
            .name("디자이너1")
            .position(Position.디자이너)
            .introduction("안녕하세요")
            .image("이미지 URL")
            .hairshop(hairshop)
            .build();
        repository.save(designer1);

        Designer designer2 = Designer.builder()
            .name("디자이너1")
            .position(Position.디자이너)
            .introduction("안녕하세요")
            .image("이미지 URL")
            .hairshop(hairshop)
            .build();
        repository.save(designer2);

        Menu menu = Menu.builder()
            .name("커트")
            .discount(0)
            .gender("남")
            .exposed_time(30)
            .image("이미지 URL")
            .price(20000)
            .type(Type.커트)
            .hairshop(hairshop)
            .build();
        menuRepository.save(menu);

        LocalDate today = LocalDate.now();
        Reservation reservation1 = Reservation.builder()
            .name("예약자1")
            .status(ReservationStatus.RESERVED)
            .time("12:00")
            .date(today)
            .phoneNumber("010-1234-5678")
            .paymentAmount(20000)
            .designer(designer1)
            .hairshop(hairshop)
            .menu(menu)
            .build();
        reservationRepository.save(reservation1);
        Reservation reservation2 = Reservation.builder()
            .name("예약자2")
            .status(ReservationStatus.RESERVED)
            .time("13:00")
            .date(today)
            .phoneNumber("010-1234-5678")
            .paymentAmount(20000)
            .designer(designer1)
            .hairshop(hairshop)
            .menu(menu)
            .build();
        reservationRepository.save(reservation2);
        Reservation reservation3 = Reservation.builder()
            .name("예약자3")
            .status(ReservationStatus.RESERVED)
            .time("14:00")
            .date(today.plusDays(1))
            .phoneNumber("010-1234-5678")
            .paymentAmount(20000)
            .designer(designer1)
            .hairshop(hairshop)
            .menu(menu)
            .build();
        reservationRepository.save(reservation3);
        Reservation reservation4 = Reservation.builder()
            .name("예약자4")
            .status(ReservationStatus.RESERVED)
            .time("12:00")
            .date(today)
            .phoneNumber("010-1234-5678")
            .paymentAmount(20000)
            .designer(designer2)
            .hairshop(hairshop)
            .menu(menu)
            .build();
        reservationRepository.save(reservation4);

        // when
        List<Designer> designers = repository.findByHairshopIdAndDate(hairshop.getId(), today);

        // then
        assertThat(designers.size(), is(2)); // 디자이너1, 디자이너2
        List<Reservation> reservations1 = designers.get(0).getReservations();
        List<Reservation> reservations2 = designers.get(1).getReservations();
        assertThat(reservations1.size(), is(2)); // 3개 중 today + 1 빠져서 2개
        assertThat(reservations2.size(), is(1));
        assertThat(reservations1.get(0).getName(), is("예약자1")); // fetch join 영속화
        assertThat(reservations1.get(1).getName(), is("예약자2"));
        assertThat(reservations2.get(0).getName(), is("예약자4"));
    }
}