package com.prgms.kokoahairshop.reservation.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationTimeRequestDto {

    private LocalDate date;

    private String reservationStartTime;

    private String reservationEndTime;
}
