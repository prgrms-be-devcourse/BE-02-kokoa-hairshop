package com.prgms.kokoahairshop.reservation1.controller;

import com.prgms.kokoahairshop.reservation1.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationSuccessResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservations;
import com.prgms.kokoahairshop.reservation1.service.ReservationService1;
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
public class ReservationController1 {

    private final ReservationService1 reservationService1;
    private final UserDetailService userDetailService;

    @PostMapping("/reservations/v1/")
    public ResponseEntity<ReservationSuccessResponseDto> reserve(@Validated @RequestBody CreateReservationRequestDto createReservationRequestDto) {
        ReservationSuccessResponseDto responseDto = reservationService1.saveReservation(createReservationRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/reservations/v1/reservation-time/hairshops/{id}")
    public ResponseEntity<List<ReservationTimeResponseDto>> reservationTimeList(@PathVariable("id") Long hairshopId, @Validated @RequestBody ReservationTimeRequestDto reservationTimeRequestDto) {
        List<ReservationTimeResponseDto> reservationTimes = reservationService1.getReservationTimeList(
            hairshopId, reservationTimeRequestDto.getDate());
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("/reservations/v1/user")
    public ResponseEntity<List<ReservationResponseDto>> reservationListByUser() {
        User user = userDetailService.getUserFromSecurityContextHolder();
        List<ReservationResponseDto> reservations = reservationService1.getReservationListByUser(user.getId());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/reservations/v1/hairshops/{id}")
    public ResponseEntity<List<ReservationResponseDto>> reservationListByHairshop(@PathVariable("id") Long hairshopId) {

        List<ReservationResponseDto> reservations = reservationService1.getReservationListByHairshop(hairshopId);
        return ResponseEntity.ok(reservations);
    }
}
