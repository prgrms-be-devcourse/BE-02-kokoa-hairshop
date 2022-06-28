package com.prgms.kokoahairshop.reservation1.convert;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.reservation1.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservation;
import com.prgms.kokoahairshop.reservation1.entity.ReservationStatus;
import java.util.stream.Collectors;

public class ReservationConverter {

    //dto -> entity
    public Reservation toReservationEntity (CreateReservationRequestDto requestDto) {
        return Reservation.builder()
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
    public ReservationResponseDto toReservationResponseDto (Reservation reservation) {
        return ReservationResponseDto.builder()
            .id(reservation.getId())
            .name(reservation.getName())
            .phoneNumber(reservation.getPhoneNumber())
            .date(reservation.getDate())
            .time(reservation.getTime())
            .status(reservation.getStatus())
            .request(reservation.getRequest())
            .paymentAmount(reservation.getPaymentAmount())
            .hairshopId(reservation.getHairshop().getId())
            .hairshopName(reservation.getHairshop().getName())
            .menuId(reservation.getMenu().getId())
            .menuName(reservation.getMenu().getName())
            .designerId(reservation.getDesigner().getId())
            .designerPosition(reservation.getDesigner().getPosition())
            .designerName(reservation.getDesigner().getName())
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
