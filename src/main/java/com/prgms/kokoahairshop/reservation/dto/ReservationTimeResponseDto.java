package com.prgms.kokoahairshop.reservation.dto;

import com.prgms.kokoahairshop.designer.entity.Position;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ReservationTimeResponseDto {
    private Position designerPosition;

    private String designerName;

    private String designerImage;

    private String designerInstruction;

    private List<String> reservationTimes;
}
