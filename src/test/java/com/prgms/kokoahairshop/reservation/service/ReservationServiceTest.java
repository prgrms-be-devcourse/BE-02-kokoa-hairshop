package com.prgms.kokoahairshop.reservation.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation.exception.ReservationNotFoundException;
import com.prgms.kokoahairshop.reservation.exception.ReservationNotReservedException;
import com.prgms.kokoahairshop.reservation.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    ReservationService service;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    DesignerRepository designerRepository;

    @Test
    void 헤어샵의_특정_날짜_예약_가능_시간들을_조회할_수_있다() {
        Hairshop hairshop = Hairshop.builder()
            .id(1L)
            .build();

        Designer designer1 = Designer.builder()
            .name("디자이너1")
            .position(Position.디자이너)
            .image("이미지 URL1")
            .introduction("안녕하세요.")
            .build();

        Designer designer2 = Designer.builder()
            .name("디자이너1")
            .position(Position.디자이너)
            .image("이미지 URL1")
            .introduction("안녕하세요.")
            .build();

        LocalDate today = LocalDate.now();
        Reservation reservation1 = Reservation.builder()
            .date(today)
            .time("12:00")
            .hairshop(hairshop)
            .designer(designer1)
            .build();
        Reservation reservation2 = Reservation.builder()
            .date(today)
            .time("13:00")
            .hairshop(hairshop)
            .designer(designer1)
            .build();
        Reservation reservation3 = Reservation.builder()
            .date(today)
            .time("14:00")
            .hairshop(hairshop)
            .designer(designer2)
            .build();
        Reservation reservation4 = Reservation.builder()
            .date(today)
            .time("15:00")
            .hairshop(hairshop)
            .designer(designer2)
            .build();

        service.getReservationTime(hairshop.getId(),
            new ReservationTimeRequestDto(today, "09:00", "18:00"));
    }

    @Test
    void 예약을_취소할_수_있다() {
        // given
        LocalDate today = LocalDate.now();
        Reservation reservation = Reservation.builder()
            .id(1L)
            .date(today.plusDays(1))
            .time("12:00")
            .status(ReservationStatus.RESERVED)
            .build();

        when(reservationRepository.findById(reservation.getId())).thenReturn(
            Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(null);

        // when & then
        service.cancelReservation(1L);
    }

    @Test
    void 예약_취소_예약이_존재하지_않으면_예외가_발생한다() {
        // given
        when(reservationRepository.findById(0L)).thenThrow(
            new ReservationNotFoundException("해당 예약이 존재하지 않습니다."));
        // when & then
        assertThrows(ReservationNotFoundException.class,
            () -> service.cancelReservation(0L));
    }

    @Test
    void 예약_취소_RESERVED가_아니면_예외가_발생한다() {
        // given
        LocalDate today = LocalDate.now();
        Reservation reservation = Reservation.builder()
            .id(1L)
            .date(today.plusDays(1))
            .time("12:00")
            .status(ReservationStatus.CANCELED)
            .build();

        when(reservationRepository.findById(reservation.getId())).thenReturn(
            Optional.of(reservation));

        // when & then
        assertThrows(ReservationNotReservedException.class,
            () -> service.cancelReservation(reservation.getId()));
    }

    @Test
    void 예약_취소_취소_가능시간이_지났으면_예외가_발생한다() {
        // given
        String hour = String.valueOf(LocalDateTime.now().getHour());
        String time = (hour.length() == 1 ? "0" + hour : hour) + ":00";
        Reservation reservation = Reservation.builder()
            .id(1L)
            .date(LocalDate.now())
            .time(time)
            .status(ReservationStatus.RESERVED)
            .build();

        when(reservationRepository.findById(reservation.getId())).thenReturn(
            Optional.of(reservation));

        // when & then
        assertThrows(ReservationCancelTimeoutException.class,
            () -> service.cancelReservation(reservation.getId()));
    }
}