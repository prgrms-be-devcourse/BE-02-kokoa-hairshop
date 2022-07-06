package com.prgms.kokoahairshop.reservation.dto;

import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationResponseDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private LocalDate date;
    private String time;
    private ReservationStatus status;
    private String request;
    private int paymentAmount;

    private Long hairshopId;
    private String hairshopName;

    private Long menuId;
    private String menuName;

    private Long designerId;
    private Position designerPosition;
    private String designerName;

}