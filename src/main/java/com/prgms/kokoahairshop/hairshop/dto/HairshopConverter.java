package com.prgms.kokoahairshop.hairshop.dto;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import org.springframework.stereotype.Component;

@Component
public class HairshopConverter {
    public Hairshop createHairshopDtoToHairshop(HairshopDto hairshopDto) {
        return Hairshop.builder()
                .name(hairshopDto.getName())
                .phoneNumber(hairshopDto.getPhoneNumber())
                .startTime(hairshopDto.getStartTime())
                .endTime(hairshopDto.getEndTime())
                .closedDay(hairshopDto.getClosedDay())
                .reservationRange(hairshopDto.getReservationRange())
                .reservationStartTime(hairshopDto.getReservationStartTime())
                .reservationEndTime(hairshopDto.getReservationEndTime())
                .sameDayAvailable(hairshopDto.getSameDayAvailable())
                .roadNameNumber(hairshopDto.getRoadNameNumber())
                .profileImg(hairshopDto.getProfileImg())
                .introduction(hairshopDto.getIntroduction())
                .build();
    }

    public HairshopDto hairshopToReadHairshopDto(Hairshop hairshop) {
        return HairshopDto.builder()
                .id(hairshop.getId())
                .name(hairshop.getName())
                .phoneNumber(hairshop.getPhoneNumber())
                .startTime(hairshop.getStartTime())
                .endTime(hairshop.getEndTime())
                .closedDay(hairshop.getClosedDay())
                .reservationRange(hairshop.getReservationRange())
                .reservationStartTime(hairshop.getReservationStartTime())
                .reservationEndTime(hairshop.getReservationEndTime())
                .sameDayAvailable(hairshop.getSameDayAvailable())
                .roadNameNumber(hairshop.getRoadNameNumber())
                .profileImg(hairshop.getProfileImg())
                .introduction(hairshop.getIntroduction())
                .userId(hairshop.getUserId())
                .createdAt(hairshop.getCreatedAt())
                .updatedAt(hairshop.getUpdatedAt())
                .build();
    }
}
