package com.prgms.kokoahairshop.reservation.dto;

import com.prgms.kokoahairshop.designer.entity.Position;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTimeResponseDto {
    private Long designerId;

    private Position designerPosition;

    private String designerName;

    private String designerImage;

    private String designerInstruction;

    private List<String> reservationTimes;
}