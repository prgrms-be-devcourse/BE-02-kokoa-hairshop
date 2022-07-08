package com.prgms.kokoahairshop.hairshop.dto;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import org.springframework.stereotype.Component;

@Component
public class HairshopConverter {

    public Hairshop convertToHairshop(CreateHairshopRequest createHairshopRequest) {
        return Hairshop.builder()
            .name(createHairshopRequest.getName())
            .phoneNumber(createHairshopRequest.getPhoneNumber())
            .startTime(createHairshopRequest.getStartTime())
            .endTime(createHairshopRequest.getEndTime())
            .closedDay(createHairshopRequest.getClosedDay())
            .reservationRange(createHairshopRequest.getReservationRange())
            .reservationStartTime(createHairshopRequest.getReservationStartTime())
            .reservationEndTime(createHairshopRequest.getReservationEndTime())
            .sameDayAvailable(createHairshopRequest.getSameDayAvailable())
            .roadNameNumber(createHairshopRequest.getRoadNameNumber())
            .profileImg(createHairshopRequest.getProfileImg())
            .introduction(createHairshopRequest.getIntroduction())
            .build();
    }

    public Hairshop convertToHairshop(ModifyHairshopRequest modifyHairshopRequest) {
        return Hairshop.builder()
            .id(modifyHairshopRequest.getId())
            .name(modifyHairshopRequest.getName())
            .phoneNumber(modifyHairshopRequest.getPhoneNumber())
            .startTime(modifyHairshopRequest.getStartTime())
            .endTime(modifyHairshopRequest.getEndTime())
            .closedDay(modifyHairshopRequest.getClosedDay())
            .reservationRange(modifyHairshopRequest.getReservationRange())
            .reservationStartTime(modifyHairshopRequest.getReservationStartTime())
            .reservationEndTime(modifyHairshopRequest.getReservationEndTime())
            .sameDayAvailable(modifyHairshopRequest.getSameDayAvailable())
            .roadNameNumber(modifyHairshopRequest.getRoadNameNumber())
            .profileImg(modifyHairshopRequest.getProfileImg())
            .introduction(modifyHairshopRequest.getIntroduction())
            .build();
    }

    public HairshopResponse convertToHairshopResponse(Hairshop hairshop) {
        return HairshopResponse.builder()
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
            .createdAt(hairshop.getCreatedAt())
            .updatedAt(hairshop.getUpdatedAt())
            .build();
    }
}
