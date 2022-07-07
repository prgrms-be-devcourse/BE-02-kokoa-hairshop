package com.prgms.kokoahairshop.reservation.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDtoDynamic;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDtoStatic;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation.exception.ReservationNotReservedException;
import com.prgms.kokoahairshop.reservation.repository.ReservationRepository;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    HairshopRepository hairshopRepository;

    @Mock
    DesignerRepository designerRepository;

    @Mock
    MenuRepository menuRepository;

    @Test
    void 예약을_생성할_수_있다() {
        // given
        CreateReservationRequestDto requestDto = CreateReservationRequestDto.builder()
            .name("예약자")
            .phoneNumber("010-1234-5678")
            .date(LocalDate.now())
            .time("12:00")
            .request("예쁘게 잘라주세요.")
            .paymentAmount(20000)
            .userId(1L)
            .hairshopId(1L)
            .designerId(1L)
            .menuId(1L)
            .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));
        when(hairshopRepository.findById(1L)).thenReturn(Optional.of(Hairshop.builder().build()));
        when(designerRepository.findById(1L)).thenReturn(Optional.of(Designer.builder().build()));
        when(menuRepository.findById(1L)).thenReturn(Optional.of(Menu.builder().build()));
        when(reservationRepository.existsByDateAndTimeAndDesignerId(LocalDate.now(), "12:00",
            1L)).thenReturn(
            false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(
            Reservation.builder().id(1L).build());

        // when & then
        assertThat(reservationService.saveDynamic(requestDto).getId(), is(1L));
    }

    @Test
    void 예약_생성_시_이미_예약이_존재하면_예외가_발생한다() {
        // given
        CreateReservationRequestDto requestDto = CreateReservationRequestDto.builder()
            .name("예약자")
            .phoneNumber("010-1234-5678")
            .date(LocalDate.now())
            .time("12:00")
            .request("예쁘게 잘라주세요.")
            .userId(1L)
            .hairshopId(1L)
            .designerId(1L)
            .menuId(1L)
            .build();
        when(designerRepository.findById(1L)).thenReturn(
            Optional.of(Designer.builder().id(1L).build()));
        when(reservationRepository.existsByDateAndTimeAndDesignerId(LocalDate.now(), "12:00",
            1L)).thenReturn(
            true);

        // when & then
        assertThrows(DuplicateReservationException.class,
            () -> reservationService.saveDynamic(requestDto));
    }

    @Test
    void 헤어샵의_특정_날짜_예약_가능_시간들을_조회할_수_있다() {
        // given
        Designer designer1 = Designer.builder()
            .name("디자이너1")
            .position(Position.DESIGNER)
            .image("디자이너_이미지_URL1")
            .introduction("안녕하세요.")
            .build();

        Designer designer2 = Designer.builder()
            .name("디자이너2")
            .position(Position.DESIGNER)
            .image("디자이너_이미지_URL2")
            .introduction("안녕하세요.")
            .build();

        LocalDate today = LocalDate.now();
        Reservation reservation1 = Reservation.builder()
            .status(ReservationStatus.RESERVED)
            .date(today)
            .time("12:00")
            .designer(designer1)
            .build();
        designer1.addReservation(reservation1);

        Reservation reservation2 = Reservation.builder()
            .status(ReservationStatus.RESERVED)
            .date(today)
            .time("13:00")
            .designer(designer1)
            .build();
        designer1.addReservation(reservation2);

        Reservation reservation3 = Reservation.builder()
            .status(ReservationStatus.CANCELED)
            .date(today)
            .time("14:00")
            .designer(designer2)
            .build();
        designer1.addReservation(reservation3);

        Reservation reservation4 = Reservation.builder()
            .status(ReservationStatus.RESERVED)
            .date(today)
            .time("15:00")
            .designer(designer2)
            .build();
        designer2.addReservation(reservation4);

        ReservationTimeRequestDtoDynamic requestDto = ReservationTimeRequestDtoDynamic.builder()
            .date(today)
            .reservationStartTime("11:00")
            .reservationEndTime("21:00")
            .build();

        when(designerRepository.findByHairshopIdAndDate(1L, today)).thenReturn(
            List.of(designer1, designer2));

        // when
        List<ReservationTimeResponseDto> responseDtos = reservationService.findReservationTimesDynamic(
            1L, requestDto);

        // then
        assertThat(responseDtos.size(), is(2));
        assertThat(responseDtos.get(0).getDesignerName(), is("디자이너1"));
        assertThat(responseDtos.get(1).getDesignerName(), is("디자이너2"));
        List<String> reservationTimes1 = responseDtos.get(0).getReservationTimes();
        assertThat(reservationTimes1.contains("12:00"), is(false));
        assertThat(reservationTimes1.contains("13:00"), is(false));
        assertThat(reservationTimes1.contains("14:00"), is(true)); // CANCELED 는 포함 X
        List<String> reservationTimes2 = responseDtos.get(1).getReservationTimes();
        assertThat(reservationTimes2.contains("15:00"), is(false));
    }

    @Test
    // TODO : 예약자를 확인할 수 있도록..?
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
        reservationService.cancelReservationByUser(1L);
    }

    @Test
    // TODO : 예약자를 확인할 수 있도록..?
    void 예약_취소_예약이_존재하지_않으면_예외가_발생한다() {
        // given
        when(reservationRepository.findById(0L)).thenThrow(
            new NotFoundException("해당 예약이 존재하지 않습니다."));

        // when & then
        assertThrows(NotFoundException.class,
            () -> reservationService.cancelReservationByUser(0L));
    }

    @Test
    // TODO : 예약자를 확인할 수 있도록..?
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
            () -> reservationService.cancelReservation(reservation.getId()));
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
            () -> reservationService.cancelReservation(reservation.getId()));
    }

    @Test
    @DisplayName("UserId를 통해서 예약리스트를 검색후 Dto로 반환성공")
    void getReservationListByUserTest() {
        // Given
        Long userId = 1L;
        given(reservationRepository.findReservationsByUserId(userId))
            .willReturn(List.of(
                createReservation(1L, "11:00"),
                createReservation(2L, "12:00"),
                createReservation(3L, "12:30")
            ));

        // When
        List<ReservationResponseDto> reservations = reservationService.findReservationsByUser(1L);

        // Then
        reservations.forEach(
            (reservation) -> log.info("{}, {}", reservation.getId(), reservation.getTime()));
        Assertions.assertThat(reservations.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("HairshopId 통해서 예약리스트를 검색후 Dto로 반환성공")
    void getReservationListByHairshopTest() {
        // Given
        Long hairshopId = 1L;
        given(reservationRepository.findReservationsByHairshopId(hairshopId))
            .willReturn(List.of(
                createReservation(1L, "11:00"),
                createReservation(2L, "12:00"),
                createReservation(3L, "12:30")
            ));

        // When
        List<ReservationResponseDto> reservations = reservationService.findReservationsByHairshop(
            1L);

        // Then
        reservations.forEach(
            (reservation) -> log.info("{}, {}", reservation.getId(), reservation.getTime()));
        Assertions.assertThat(reservations.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("hairshopId와 날짜로 예약 가능한 시간 조회후 반환")
    void getReservationTimeListTest() {
        // Given
        Long hairshopId = 1L;
        ReservationTimeRequestDtoStatic requestDto = ReservationTimeRequestDtoStatic.builder()
            .date(LocalDate.of(2022, 6, 28))
            .build();
        given(designerRepository.findDesignerFetchJoinByHairshopIdAndDate(hairshopId,
            requestDto.getDate()))
            .willReturn(List.of(
                    createDesigner(1L, hairshopId),
                    createDesigner(2L, hairshopId),
                    createDesigner(3L, hairshopId),
                    createDesigner(4L, hairshopId)
                )
            );

        // When
        List<ReservationTimeResponseDto> designers = reservationService.findReservationTimesStatic(
            hairshopId, requestDto);

        // Then
        designers.forEach(
            (designer) -> log.info("{}, {}", designer.getDesignerName(),
                designer.getReservationTimes()));
        Assertions.assertThat(designers.size()).isEqualTo(4);
    }


    private Reservation createReservation(Long id, String time) {
        return Reservation.builder()
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