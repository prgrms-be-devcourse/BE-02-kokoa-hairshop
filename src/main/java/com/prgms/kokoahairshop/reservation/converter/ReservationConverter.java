package com.prgms.kokoahairshop.reservation.converter;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.reservation.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import com.prgms.kokoahairshop.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationConverter {

    public static Reservation toEntity(CreateReservationRequestDto requestDto, User user,
        Hairshop hairshop, Designer designer, Menu menu) {
        return Reservation.builder()
            .name(requestDto.getName())
            .status(ReservationStatus.RESERVED)
            .request(requestDto.getRequest())
            .date(requestDto.getDate())
            .paymentAmount(requestDto.getPaymentAmount())
            .phoneNumber(requestDto.getPhoneNumber())
            .time(requestDto.getTime())
            .user(user)
            .hairshop(hairshop)
            .designer(designer)
            .menu(menu)
            .build();
    }

    public static ReservationResponseDto toReservationResponseDto(Reservation reservation) {
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

    public static ReservationTimeResponseDto toReservationTimeResponseDtoStatic(Designer designer) {
        return ReservationTimeResponseDto.builder()
            .designerId(designer.getId())
            .designerPosition(designer.getPosition())
            .designerName(designer.getName())
            .designerImage(designer.getImage())
            .designerInstruction(designer.getIntroduction())
            .reservationTimes(designer.getReservationTimes()
                .stream().map(reservationTime -> reservationTime.getTime())
                .collect(Collectors.toList()))
            .build();
    }

    public static ReservationTimeResponseDto toReservationTimeResponseDtoDynamic(Designer designer,
        List<String> reservationTimes) {
        return ReservationTimeResponseDto.builder()
            .designerId(designer.getId())
            .designerPosition(designer.getPosition())
            .designerImage(designer.getImage())
            .designerName(designer.getName())
            .designerInstruction(designer.getIntroduction())
            .reservationTimes(reservationTimes)
            .build();
    }
}