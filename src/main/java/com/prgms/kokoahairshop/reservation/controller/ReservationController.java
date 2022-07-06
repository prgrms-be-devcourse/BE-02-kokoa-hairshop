package com.prgms.kokoahairshop.reservation.controller;

import com.prgms.kokoahairshop.reservation.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation.dto.CreateReservationResponseDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDtoDynamic;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDtoStatic;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation.service.ReservationService;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.service.UserDetailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PostMapping("/v1/reservations")
    public ResponseEntity<CreateReservationResponseDto> reserveStatic(
        @Validated @RequestBody CreateReservationRequestDto requestDto) {
        CreateReservationResponseDto responseDto = reservationService.saveStatic(
            requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/v2/reservations")
    public ResponseEntity<CreateReservationResponseDto> reserveDynamic(
        @Validated @RequestBody CreateReservationRequestDto requestDto) {
        CreateReservationResponseDto responseDto = reservationService.saveDynamic(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/reservations/user")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByUser() {
        User user = userDetailService.getUserFromSecurityContextHolder();
        List<ReservationResponseDto> responseDtos = reservationService.findReservationsByUser(
            user.getId());

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/reservations/hairshops/{hairshopId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByHairshop(
        @PathVariable Long hairshopId) {

        List<ReservationResponseDto> responseDtos = reservationService.findReservationsByHairshop(
            hairshopId);

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/v1/reservations/reservation-time/hairshops/{hairshopId}")
    public ResponseEntity<List<ReservationTimeResponseDto>> reservationTimeList(
        @PathVariable Long hairshopId,
        @Validated @RequestBody ReservationTimeRequestDtoStatic requestDto) {
        List<ReservationTimeResponseDto> responseDtos = reservationService.findReservationTimesStatic(
            hairshopId, requestDto);

        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping("/v2/reservations/reservation-times/hairshops/{hairshopId}")
    public ResponseEntity<List<ReservationTimeResponseDto>> getReservationTimes(
        @PathVariable Long hairshopId,
        @Validated @RequestBody ReservationTimeRequestDtoDynamic requestDto) {
        List<ReservationTimeResponseDto> responseDtos = reservationService.findReservationTimesDynamic(
            hairshopId, requestDto);

        return ResponseEntity.ok().body(responseDtos);
    }

    @PatchMapping("/v2/reservations/{reservationId}/user")
    public ResponseEntity<Object> cancelReservationByUserDynamic(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/v2/reservations/{reservationId}/hairshop")
    public ResponseEntity<Object> cancelReservationByHairshopDynamic(
        @PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/v1/reservations/reservationTimes")
    public ResponseEntity<Void> createFirstReservationTime() {
        reservationService.createReservationTimes();
        return ResponseEntity.ok().build();
    }
}