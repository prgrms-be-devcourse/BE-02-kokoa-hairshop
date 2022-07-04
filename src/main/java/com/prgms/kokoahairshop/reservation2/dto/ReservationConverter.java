package com.prgms.kokoahairshop.reservation2.dto;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import com.prgms.kokoahairshop.reservation2.entity.ReservationStatus;
import com.prgms.kokoahairshop.user.entity.User;
import java.util.List;

public class ReservationConverter {

    public static Reservation toEntity(ReservationRequestDto requestDto, User user,
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

    public static ReservationTimeResponseDto toReservationTimeResponseDto(Designer designer,
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