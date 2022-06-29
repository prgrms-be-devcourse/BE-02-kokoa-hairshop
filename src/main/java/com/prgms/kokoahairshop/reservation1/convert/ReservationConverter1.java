package com.prgms.kokoahairshop.reservation1.convert;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.reservation1.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservations;
import com.prgms.kokoahairshop.reservation1.entity.ReservationStatus;
import java.util.stream.Collectors;

public class ReservationConverter1 {

    //dto -> entity
    public Reservations toReservationEntity (CreateReservationRequestDto requestDto) {
        return Reservations.builder()
            .name(requestDto.getName())
            .phoneNumber(requestDto.getPhoneNumber())
            .date(requestDto.getDate())
            .time(requestDto.getTime())
            .status(ReservationStatus.RESERVED)
            .request(requestDto.getRequest())
            .paymentAmount(requestDto.getPaymentAmount())
            .build();
    }

    //entity -> dto
    public ReservationResponseDto toReservationResponseDto (Reservations reservations) {
        return ReservationResponseDto.builder()
            .id(reservations.getId())
            .name(reservations.getName())
            .phoneNumber(reservations.getPhoneNumber())
            .date(reservations.getDate())
            .time(reservations.getTime())
            .status(reservations.getStatus())
            .request(reservations.getRequest())
            .paymentAmount(reservations.getPaymentAmount())
            .hairshopId(reservations.getHairshop().getId())
            .hairshopName(reservations.getHairshop().getName())
            .menuId(reservations.getMenu().getId())
            .menuName(reservations.getMenu().getName())
            .designerId(reservations.getDesigner().getId())
            .designerPosition(reservations.getDesigner().getPosition())
            .designerName(reservations.getDesigner().getName())
            .build();
    }

    // entity -> dto
    public ReservationTimeResponseDto toReservationTimeResponseDto (Designer designer) {
        return ReservationTimeResponseDto.builder()
            .designerPosition(designer.getPosition())
            .designerName(designer.getName())
            .designerInstruction(designer.getIntroduction())
            .reservationTimes(designer.getReservationTimes()
                .stream().map(reservationTime -> reservationTime.getTime())
                .collect(Collectors.toList()))
            .build();
    }

}
