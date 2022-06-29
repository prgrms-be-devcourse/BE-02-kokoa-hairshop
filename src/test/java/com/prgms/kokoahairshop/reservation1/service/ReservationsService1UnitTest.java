package com.prgms.kokoahairshop.reservation1.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.reservation1.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservations;
import com.prgms.kokoahairshop.reservation1.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation1.repository.ReservationRepository1;
import com.prgms.kokoahairshop.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReservationsService1UnitTest {

    @InjectMocks
    private ReservationService1Impl reservationService;

    @Mock
    private ReservationRepository1 reservationRepository1;

    @Mock
    private DesignerRepository designerRepository;


    @Test
    @DisplayName("UserId를 통해서 예약리스트를 검색후 Dto로 반환성공")
    void getReservationListByUserTest() {
        // Given
        Long userId = 1L;
        given(reservationRepository1.findReservationsByUserId(userId))
            .willReturn(List.of(
                createReservation(1L, "11:00"),
                createReservation(2L, "12:00"),
                createReservation(3L, "12:30")
            ));

        // When
        List<ReservationResponseDto> reservations = reservationService.getReservationListByUser(1L);

        // Then
        reservations.forEach(
            (reservation) -> log.info("{}, {}", reservation.getId(), reservation.getTime()));
        assertThat(reservations.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("HairshopId 통해서 예약리스트를 검색후 Dto로 반환성공")
    void getReservationListByHairshopTest() {
        // Given
        Long hairshopId = 1L;
        given(reservationRepository1.findReservationsByHairshopId(hairshopId))
            .willReturn(List.of(
                createReservation(1L, "11:00"),
                createReservation(2L, "12:00"),
                createReservation(3L, "12:30")
            ));

        // When
        List<ReservationResponseDto> reservations = reservationService.getReservationListByHairshop(
            1L);

        // Then
        reservations.forEach(
            (reservation) -> log.info("{}, {}", reservation.getId(), reservation.getTime()));
        assertThat(reservations.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("hairshopId와 날짜로 예약 가능한 시간 조회후 반환")
    void getReservationTimeListTest() {
        // Given
        Long hairshopId = 1L;
        Long designerId = 1L;
        LocalDate date = LocalDate.of(2022, 6, 28);
        given(designerRepository.findDesignerFetchJoinByHairshopIdAndDate(hairshopId, date))
            .willReturn(List.of(
                    createDesigner(1L, hairshopId),
                    createDesigner(2L, hairshopId),
                    createDesigner(3L, hairshopId),
                    createDesigner(4L, hairshopId)
                )
            );

        // When
        List<ReservationTimeResponseDto> designers = reservationService.getReservationTimeList(
            hairshopId, date);

        // Then
        designers.forEach(
            (designer) -> log.info("{}, {}", designer.getDesignerName(),
                designer.getReservationTimes()));
        assertThat(designers.size()).isEqualTo(4);
    }


    private Reservations createReservation(Long id, String time) {
        return Reservations.builder()
            .id(id)
            .time(time)
            .user(User.builder().build())
            .hairshop(Hairshop.builder().build())
            .designer(Designer.builder().build())
            .menu(Menu.builder().build())
            .build();
    }

    private Designer createDesigner(Long designerId, Long hairshopId) {
        return Designer.builder()
            .id(designerId)
            .name("designer" + designerId)
            .hairshop(Hairshop.builder()
                .id(hairshopId)
                .build())
            .reservationTimes(List.of(
                    createReservationTime(1L, "11:00"),
                    createReservationTime(2L, "11:30"),
                    createReservationTime(3L, "12:30"),
                    createReservationTime(4L, "13:00")
                )
            )
            .build();
    }

    private ReservationTime createReservationTime(Long id, String time) {
        return ReservationTime.builder()
            .id(id)
            .time(time)
            .reserved(false)
            .build();
    }

}