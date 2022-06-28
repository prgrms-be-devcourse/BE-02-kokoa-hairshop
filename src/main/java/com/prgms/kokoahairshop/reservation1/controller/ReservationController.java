package com.prgms.kokoahairshop.reservation1.controller;

import com.prgms.kokoahairshop.reservation1.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservation;
import com.prgms.kokoahairshop.reservation1.service.ReservationService;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.service.UserDetailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final UserDetailService userDetailService;

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> reserve(@Validated @RequestBody CreateReservationRequestDto createReservationRequestDto) {
        Reservation reservation = reservationService.saveReservation(createReservationRequestDto);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/reservations/reservation-time/hairshops/{id}")
    public ResponseEntity<List<ReservationTimeResponseDto>> reservationTimeList(@PathVariable("id") Long hairshopId, @Validated @RequestBody ReservationTimeRequestDto reservationTimeRequestDto) {
        List<ReservationTimeResponseDto> reservationTimes = reservationService.getReservationTimeList(
            hairshopId, reservationTimeRequestDto.getDate());
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("/reservations/user")
    public ResponseEntity<List<ReservationResponseDto>> reservationListByUser() {
        User user = userDetailService.getUserFromSecurityContextHolder();
        List<ReservationResponseDto> reservations = reservationService.getReservationListByUser(user.getId());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/reservations/hairshops/{id}")
    public ResponseEntity<List<ReservationResponseDto>> reservationListByHairshop(@PathVariable("id") Long hairshopId) {

        List<ReservationResponseDto> reservations = reservationService.getReservationListByHairshop(hairshopId);
        return ResponseEntity.ok(reservations);
    }
}
