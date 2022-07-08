package com.prgms.kokoahairshop.hairshop.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HairshopResponse {

    private Long id;

    private String name;

    private String phoneNumber;

    private String startTime;

    private String endTime;

    private String closedDay;

    private String reservationRange;

    private String reservationStartTime;

    private String reservationEndTime;

    private Boolean sameDayAvailable;

    private String roadNameNumber;

    private String profileImg;

    private String introduction;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
