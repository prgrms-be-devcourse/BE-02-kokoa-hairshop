package com.prgms.kokoahairshop.reservation1.service;

import com.prgms.kokoahairshop.reservation1.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservation;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    Reservation saveReservation(CreateReservationRequestDto createReservationRequestDto);
    List<ReservationResponseDto> getReservationListByUser(Long userId);
    List<ReservationResponseDto> getReservationListByHairshop(Long hairshopId);
    List<ReservationTimeResponseDto> getReservationTimeList(Long hairShopId, LocalDate date);
}
