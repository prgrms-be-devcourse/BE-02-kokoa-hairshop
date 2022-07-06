package com.prgms.kokoahairshop.designer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation.entity.ReservationTime;
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

@Slf4j
@SpringBootTest
@Transactional
class DesignerRepositoryTest {

    @Autowired
    private DesignerRepository designerRepository;


    @PersistenceContext
    EntityManager em;

    Hairshop hairshop;
    Designer designer;
    Menu menu;
    Reservation reservation;
    ReservationTime reservationTime1;
    ReservationTime reservationTime2;

    @BeforeEach
    void beforeEach() {
        User user = User.builder()
            .email("example2@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        em.persist(user);

        hairshop = Hairshop.builder()
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
            .user(user)
            .build();
        em.persist(hairshop);

        designer = Designer.builder()
            .name("디자이너1")
            .position(Position.DESIGNER)
            .introduction("안녕하세요")
            .image("이미지 URL")
            .hairshop(hairshop)
            .build();
        em.persist(designer);

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
        em.persist(reservation);

        reservationTime1 = ReservationTime.builder()
            .date(LocalDate.now())
            .time("11:00")
            .reserved(false)
            .designer(designer)
            .build();
        em.persist(reservationTime1);

        reservationTime2 = ReservationTime.builder()
            .date(LocalDate.now())
            .time("11:00")
            .reserved(false)
            .designer(designer)
            .build();

//        reservationTime.setDesigner(designer);
        em.persist(reservationTime2);
    }

    @Test
    @DisplayName("designer와 ReservationTimes 테이블을 fetch join 해서 가져올 수 있다.")
    @Rollback(value = false)
    void findDesignerFetchJoinByHairshopIdAndDateTest() {

        // When
        List<Designer> designers = designerRepository.findDesignerFetchJoinByHairshopIdAndDate(
            hairshop.getId(), LocalDate.now());

        // Then
        log.info("{}", designers.size());
        assertThat(designers.get(0).getReservationTimes()).isIn(reservationTime1, reservationTime2);
    }
}
