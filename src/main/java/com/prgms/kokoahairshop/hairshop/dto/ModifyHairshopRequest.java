package com.prgms.kokoahairshop.hairshop.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyHairshopRequest {
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
}
