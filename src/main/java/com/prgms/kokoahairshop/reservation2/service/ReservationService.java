package com.prgms.kokoahairshop.reservation2.service;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.common.util.TimeUtil;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation2.dto.ReservationConverter;
import com.prgms.kokoahairshop.reservation2.dto.ReservationRequestDto;
import com.prgms.kokoahairshop.reservation2.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation2.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation2.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import com.prgms.kokoahairshop.reservation2.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation2.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation2.exception.ReservationNotReservedException;
import com.prgms.kokoahairshop.reservation2.repository.ReservationRepository;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;

    private final UserRepository userRepository;

    private final HairshopRepository hairshopRepository;

    private final DesignerRepository designerRepository;

    private final MenuRepository menuRepository;

    @Transactional
    public ReservationResponseDto save(ReservationRequestDto requestDto) {
        Optional<Designer> maybeDesigner = designerRepository.findById(requestDto.getDesignerId());
        if (maybeDesigner.isEmpty()) {
            throw new NotFoundException("해당 디자이너가 존재하지 않습니다.");
        }

        if (repository.existsByDateAndTimeAndDesignerId(requestDto.getDate(), requestDto.getTime(),
            requestDto.getDesignerId())) {
            throw new DuplicateReservationException("이미 해당 디자이너의 예약이 존재합니다.");
        }

        Optional<User> maybeUser = userRepository.findById(requestDto.getUserId());
        if (maybeUser.isEmpty()) {
            throw new NotFoundException("해당 사용자가 존재하지 않습니다.");
        }

        Optional<Hairshop> maybeHairshop = hairshopRepository.findById(requestDto.getHairshopId());
        if (maybeHairshop.isEmpty()) {
            throw new NotFoundException("해당 헤어샵이 존재하지 않습니다.");
        }

        Optional<Menu> maybeMenu = menuRepository.findById(requestDto.getMenuId());
        if (maybeMenu.isEmpty()) {
            throw new NotFoundException("해당 메뉴가 존재하지 않습니다.");
        }

        Reservation reservation = ReservationConverter.toEntity(requestDto, maybeUser.get(),
            maybeHairshop.get(), maybeDesigner.get(), maybeMenu.get());
        Reservation savedReservation = repository.save(reservation);

        return ReservationResponseDto.builder().id(savedReservation.getId()).build();
    }

    @Transactional(readOnly = true)
    public List<ReservationTimeResponseDto> getReservationTime(Long hairshopId,
        ReservationTimeRequestDto requestDto) {
        List<Designer> designers = designerRepository.findByHairshopIdAndDate(hairshopId, requestDto.getDate());


        List<String> times = TimeUtil.getTimesFromStartAndEndTime(
            requestDto.getReservationStartTime(), requestDto.getReservationEndTime());
        List<ReservationTimeResponseDto> responseDtos = new ArrayList<>();
        for (Designer designer : designers) {
            List<String> reservationTimes = new ArrayList<>(times);
            List<Reservation> reservations = designer.getReservations();
            for (Reservation reservation : reservations) {
                if (reservation.getStatus() == ReservationStatus.RESERVED) {
                    reservationTimes.remove(reservation.getTime());
                }
            }

            responseDtos.add(ReservationConverter.toReservationTimeResponseDto(
                designer, reservationTimes));
        }

        List<Designer> allDesigners = designerRepository.findByHairshopId(hairshopId);
        for(Designer designer : allDesigners) {
            boolean contains = false;
            Long id = designer.getId();
            for(ReservationTimeResponseDto responseDto : responseDtos) {
                if(responseDto.getDesignerId() == id) {
                    contains = true;
                    break;
                }
            }
            if(!contains) {
                responseDtos.add( ReservationConverter.toReservationTimeResponseDto(
                    designer, times
                ));
            }
        }

        return responseDtos;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> maybeReservation = repository.findById(reservationId);
        if (maybeReservation.isEmpty()) {
            throw new NotFoundException("해당 예약이 존재하지 않습니다.");
        }

        Reservation reservation = maybeReservation.get();
        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new ReservationNotReservedException("해당 예약은 예약 상태가 아닙니다.");
        }

        checkCancelTimeout(reservation);

        reservation.changeStatus(ReservationStatus.CANCELED);
        repository.save(reservation);
    }

    public void checkCancelTimeout(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = reservation.getDate();
        String[] time = reservation.getTime().split(":");
        int hour = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[1]);
        LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth(),
            date.getDayOfMonth(), hour, min);
        LocalDateTime limitDateTime = dateTime.minusHours(2);

        if (now.isAfter(limitDateTime)) {
            throw new ReservationCancelTimeoutException("해당 예약의 취소 가능시간이 초과하였습니다.");
        }
    }
}
