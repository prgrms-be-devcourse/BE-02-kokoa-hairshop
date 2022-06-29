package com.prgms.kokoahairshop.reservation1.service;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation1.convert.ReservationConverter;
import com.prgms.kokoahairshop.reservation1.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservation;
import com.prgms.kokoahairshop.reservation1.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation1.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation1.exception.ReservationNotFoundException;
import com.prgms.kokoahairshop.reservation1.repository.ReservationRepository;
import com.prgms.kokoahairshop.reservation1.repository.ReservationTimeRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final HairshopRepository hairshopRepository;
    private final DesignerRepository designerRepository;
    private final MenuRepository menuRepository;

    private final ReservationConverter reservationConverter = new ReservationConverter();


    @Override
    @Transactional
    public Reservation saveReservation(CreateReservationRequestDto createReservationRequestDto) {
        ReservationTime reservationTime = reservationTimeRepository.findReservationTimeByDesignerIdAndDateAndTime(
                createReservationRequestDto.getDesignerId(),
                createReservationRequestDto.getDate(),
                createReservationRequestDto.getTime())
            .orElseThrow(() -> new ReservationNotFoundException("예약 가능한 시간이 아닙니다."));
        if (reservationTime.getReserved()) {
            throw new DuplicateReservationException("이미 예약된 시간입니다.");
        }

        Reservation reservation = reservationConverter.toReservationEntity(createReservationRequestDto);
        Hairshop hairshop = hairshopRepository.findById(createReservationRequestDto.getHairshopId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 헤어샵입니다."));
        Designer designer = designerRepository.findById(createReservationRequestDto.getDesignerId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 디자이너입니다."));
        Menu menu = menuRepository.findById(createReservationRequestDto.getMenuId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 시술 메뉴입니다."));

        reservation.setHairshop(hairshop);
        reservation.setDesigner(designer);
        reservation.setMenu(menu);

        return reservationRepository.save(reservation);
    }

    //예약자별 예약 조회
    @Override
    public List<ReservationResponseDto> getReservationListByUser(Long userId) {
        return reservationRepository.findReservationsByUserId(userId)
            .stream()
            .map((reservation) -> reservationConverter.toReservationResponseDto(reservation))
            .collect(Collectors.toList());
    }

    //헤어샵별 예약 조회
    @Override
    public List<ReservationResponseDto> getReservationListByHairshop(Long hairshopId) {
        return reservationRepository.findReservationsByHairshopId(hairshopId)
            .stream()
            .map((reservation) -> reservationConverter.toReservationResponseDto(reservation))
            .collect(Collectors.toList());
    }

    //예약 가능한 시간 검색
    @Override
    public List<ReservationTimeResponseDto> getReservationTimeList(Long hairShopId,
        LocalDate date) {
        return designerRepository.findDesignerFetchJoinByHairshopIdAndDate(hairShopId, date)
            .stream().map(designer -> reservationConverter.toReservationTimeResponseDto(designer))
            .collect(Collectors.toList());
    }


}
