package com.prgms.kokoahairshop.reservation.dto;

import com.prgms.kokoahairshop.designer.entity.Designer;
import java.util.List;

public class ReservationConverter {

    public static ReservationTimeResponseDto toReservationTimeResponseDto(Designer designer,
        List<String> reservationTimes) {
        return ReservationTimeResponseDto.builder()
            .designerPosition(designer.getPosition())
            .designerImage(designer.getImage())
            .designerName(designer.getName())
            .designerInstruction(designer.getIntroduction())
            .reservationTimes(reservationTimes)
            .build();
    }
}