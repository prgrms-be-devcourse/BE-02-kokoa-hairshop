package com.prgms.kokoahairshop.reservation1.repository;

import static org.assertj.core.api.Assertions.*;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.reservation1.entity.ReservationTime;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
class ReservationTimeRepositoryTest {

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("ReservationTime 저장 성공")
    @Rollback(false)
    void saveTest() {
        // Given
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

        Designer designer = Designer.builder()
            .name("디자이너1")
            .position(Position.디자이너)
            .introduction("안녕하세요")
            .image("이미지 URL")
            .hairshop(hairshop)
            .build();

        em.persist(hairshop);
        em.persist(designer);

        ReservationTime reservationTime = ReservationTime.builder()
            .date(LocalDate.now())
            .time("11:00")
            .reserved(true)
            .hairshop(hairshop)
            .designer(designer)
            .build();

        // When
        reservationTimeRepository.save(reservationTime);

        // Then
        List<ReservationTime> findAll = reservationTimeRepository.findAll();
        assertThat(findAll.size()).isEqualTo(1);
    }

}