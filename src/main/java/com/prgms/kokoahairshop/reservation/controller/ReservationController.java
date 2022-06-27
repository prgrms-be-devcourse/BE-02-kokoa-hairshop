package com.prgms.kokoahairshop.reservation.controller;

import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("com.prgms.kokoahairshop.reservation.controller")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations/reservation-times/hairshops/{hairshopId}")
    public ResponseEntity<List<ReservationTimeResponseDto>> getReservationTime(
        @PathVariable Long hairshopId,
        @RequestBody ReservationTimeRequestDto reservationTimeRequestDto) {
        List<ReservationTimeResponseDto> responseDtos = reservationService.getReservationTime(
            hairshopId, reservationTimeRequestDto);
        return ResponseEntity.ok()
            .body(responseDtos);
    }

    @PatchMapping("/reservations/{reservationId}/user")
    public ResponseEntity<Object> cancelReservationUser(@PathVariable Long reservationId) {
        // user 정보 검증

        reservationService.cancelReservation(reservationId);

        return ResponseEntity.noContent()
            .build();
    }

    @PatchMapping("/reservations/{reservationId}/hairshop")
    public ResponseEntity<Object> cancelReservationHairshop(@PathVariable Long reservationId) {
        // user 정보 검증

        reservationService.cancelReservation(reservationId);

        return ResponseEntity.noContent()
            .build();
    }
}
