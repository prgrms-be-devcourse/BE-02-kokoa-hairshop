package com.prgms.kokoahairshop.reservation.service;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.reservation.dto.ReservationConverter;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation.exception.ReservationNotFoundException;
import com.prgms.kokoahairshop.reservation.exception.ReservationNotReservedException;
import com.prgms.kokoahairshop.reservation.repository.ReservationRepository;
import com.prgms.kokoahairshop.util.TimeUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;

    private final DesignerRepository designerRepository;

    @Transactional
    public List<ReservationTimeResponseDto> getReservationTime(Long hairshopId,
        ReservationTimeRequestDto requestDto) {
        List<Designer> designers = designerRepository.findByHairshopIdAndDate(hairshopId,
            requestDto.getDate());
        List<String> times = TimeUtil.getTimesFromStartAndEndTime(
            requestDto.getReservationStartTime(), requestDto.getReservationEndTime());
        List<ReservationTimeResponseDto> responseDtos = new ArrayList<>();
        for (Designer designer : designers) {
            List<String> reservationTimes = new ArrayList<>(times);
            List<Reservation> reservations = designer.getReservations();
            for (Reservation reservation : reservations) {
                if(reservation.getStatus() == ReservationStatus.RESERVED) {
                    reservationTimes.remove(reservation.getTime());
                }
            }

            responseDtos.add(ReservationConverter.toReservationTimeResponseDto(
                designer, reservationTimes));
        }

        return responseDtos;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> maybeReservation = repository.findById(reservationId);
        if (maybeReservation.isEmpty()) {
            throw new ReservationNotFoundException("해당 예약이 존재하지 않습니다.");
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
