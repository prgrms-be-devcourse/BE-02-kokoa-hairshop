package com.prgms.kokoahairshop.reservation2.controller;

import com.prgms.kokoahairshop.reservation2.dto.ReservationRequestDto;
import com.prgms.kokoahairshop.reservation2.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation2.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation2.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation2.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("com.prgms.kokoahairshop.reservation.controller")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> save(
        @Validated @RequestBody ReservationRequestDto requestDto) {
        return ResponseEntity.ok()
            .body(service.save(requestDto));
    }

    @GetMapping("/reservations/reservation-times/hairshops/{hairshopId}")
    public ResponseEntity<List<ReservationTimeResponseDto>> getReservationTimes(
        @PathVariable Long hairshopId,
        @Validated @RequestBody ReservationTimeRequestDto reservationTimeRequestDto) {
        List<ReservationTimeResponseDto> responseDtos = service.getReservationTime(
            hairshopId, reservationTimeRequestDto);

        return ResponseEntity.ok()
            .body(responseDtos);
    }

    @PatchMapping("/reservations/{reservationId}/user")
    public ResponseEntity<Object> cancelReservationUser(@PathVariable Long reservationId) {
        service.cancelReservation(reservationId);

        return ResponseEntity.noContent()
            .build();
    }

    @PatchMapping("/reservations/{reservationId}/hairshop")
    public ResponseEntity<Object> cancelReservationHairshop(@PathVariable Long reservationId) {
        service.cancelReservation(reservationId);

        return ResponseEntity.noContent()
            .build();
    }
}
