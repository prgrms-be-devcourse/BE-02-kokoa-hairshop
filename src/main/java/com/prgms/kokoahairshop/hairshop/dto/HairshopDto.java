package com.prgms.kokoahairshop.hairshop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class HairshopDto {
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
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
