package com.prgms.kokoahairshop.reservation.service;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.common.util.TimeUtil;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation.converter.ReservationConverter;
import com.prgms.kokoahairshop.reservation.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation.dto.CreateReservationResponseDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDtoDynamic;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeRequestDtoStatic;
import com.prgms.kokoahairshop.reservation.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation.entity.Reservation;
import com.prgms.kokoahairshop.reservation.entity.ReservationStatus;
import com.prgms.kokoahairshop.reservation.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation.exception.ReservationCancelTimeoutException;
import com.prgms.kokoahairshop.reservation.exception.ReservationNotReservedException;
import com.prgms.kokoahairshop.reservation.repository.ReservationRepository;
import com.prgms.kokoahairshop.reservation.repository.ReservationTimeRepository;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationTimeRepository reservationTimeRepository;

    private final UserRepository userRepository;

    private final HairshopRepository hairshopRepository;

    private final DesignerRepository designerRepository;

    private final MenuRepository menuRepository;

    @Transactional
    public CreateReservationResponseDto saveStatic(
        CreateReservationRequestDto requestDto) {
        ReservationTime reservationTime = reservationTimeRepository.findReservationTimeByDesignerIdAndDateAndTime(
                requestDto.getDesignerId(),
                requestDto.getDate(),
                requestDto.getTime())
            .orElseThrow(() -> new NotFoundException("예약 가능한 시간이 아닙니다."));
        if (reservationTime.getReserved()) {
            throw new DuplicateReservationException("이미 예약된 시간입니다.");
        }

        User user = userRepository.findById(requestDto.getUserId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
        Hairshop hairshop = hairshopRepository.findById(requestDto.getHairshopId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 헤어샵입니다."));
        Designer designer = designerRepository.findById(requestDto.getDesignerId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 디자이너입니다."));
        Menu menu = menuRepository.findById(requestDto.getMenuId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 시술 메뉴입니다."));

        Reservation reservation = ReservationConverter.toEntity(requestDto, user, hairshop,
            designer, menu);

        reservationRepository.save(reservation);
        reservationTime.changeReserved();
        reservationTimeRepository.save(reservationTime);

        return CreateReservationResponseDto.builder().id(reservation.getId()).build();
    }

    @Transactional
    public CreateReservationResponseDto saveDynamic(CreateReservationRequestDto requestDto) {
        Optional<Designer> maybeDesigner = designerRepository.findById(requestDto.getDesignerId());
        if (maybeDesigner.isEmpty()) {
            throw new NotFoundException("해당 디자이너가 존재하지 않습니다.");
        }

        if (reservationRepository.existsByDateAndTimeAndDesignerId(requestDto.getDate(),
            requestDto.getTime(),
            requestDto.getDesignerId())) {
            throw new DuplicateReservationException(
                "이미 해당 디자이너의 예약이 존재합니다.");
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
        Reservation savedReservation = reservationRepository.save(reservation);

        return CreateReservationResponseDto.builder().id(savedReservation.getId()).build();
    }

    public List<ReservationResponseDto> findReservationsByUser(Long userId) {
        return reservationRepository.findReservationsByUserId(userId)
            .stream()
            .map(ReservationConverter::toReservationResponseDto)
            .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> findReservationsByHairshop(Long hairshopId) {
        return reservationRepository.findReservationsByHairshopId(hairshopId)
            .stream()
            .map(ReservationConverter::toReservationResponseDto)
            .collect(Collectors.toList());
    }

    public List<ReservationTimeResponseDto> findReservationTimesStatic(Long hairshopId,
        ReservationTimeRequestDtoStatic requestDto) {
        return designerRepository.findDesignerFetchJoinByHairshopIdAndDate(hairshopId,
                requestDto.getDate())
            .stream().map(ReservationConverter::toReservationTimeResponseDtoStatic)
            .collect(Collectors.toList());
    }

    public List<ReservationTimeResponseDto> findReservationTimesDynamic(Long hairshopId,
        ReservationTimeRequestDtoDynamic requestDto) {
        List<Designer> reservedDesigner = designerRepository.findByHairshopIdAndDate(hairshopId,
            requestDto.getDate());
        List<Designer> allDesigners = designerRepository.findByHairshopId(hairshopId);

        List<String> allTimes = TimeUtil.getTimesFromStartAndEndTime(
            requestDto.getReservationStartTime(), requestDto.getReservationEndTime());
        List<ReservationTimeResponseDto> responseDtos = new ArrayList<>();
        for (Designer designer : reservedDesigner) {
            List<String> reservationTimes = new ArrayList<>(allTimes);
            List<Reservation> reservations = designer.getReservations();
            for (Reservation reservation : reservations) {
                if (reservation.getStatus() == ReservationStatus.RESERVED) {
                    reservationTimes.remove(reservation.getTime());
                }
            }

            responseDtos.add(ReservationConverter.toReservationTimeResponseDtoDynamic(
                designer, reservationTimes));
        }

        for (Designer designer : allDesigners) {
            boolean contains = false;
            for (ReservationTimeResponseDto responseDto : responseDtos) {
                if (Objects.equals(responseDto.getDesignerId(), designer.getId())) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                responseDtos.add(ReservationConverter.toReservationTimeResponseDtoDynamic(
                    designer, allTimes));
            }
        }

        return responseDtos;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> maybeReservation = reservationRepository.findById(reservationId);
        if (maybeReservation.isEmpty()) {
            throw new NotFoundException("해당 예약이 존재하지 않습니다.");
        }

        Reservation reservation = maybeReservation.get();
        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new ReservationNotReservedException("해당 예약은 예약 상태가 아닙니다.");
        }

        checkCancelTimeout(reservation);

        reservation.changeStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
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

    @Transactional
    public void createReservationTimes() {
        List<Designer> designers = designerRepository.findAllDesignerFetchJoin();
        LocalDate date = LocalDate.now();
        StringTokenizer st;

        for (int i = 0; i < 30; i++) {
            LocalDate date2 = date.plusDays(i);

            for (Designer designer : designers) {
                String reservationStartTime = designer.getHairshop().getReservationStartTime();
                String reservationEndTime = designer.getHairshop().getReservationEndTime();
                st = new StringTokenizer(reservationStartTime, ":");
                int startHour = Integer.parseInt(st.nextToken());
                int startMinute = Integer.parseInt(st.nextToken());
                st = new StringTokenizer(reservationEndTime, ":");
                int endHour = Integer.parseInt(st.nextToken());
                int endMinute = Integer.parseInt(st.nextToken());

                while (startHour <= endHour) {
                    if (startHour == endHour && startMinute > endMinute) {
                        break;
                    }
                    String strHour = "";
                    String strMinute;
                    if (startHour < 10) {
                        strHour = "0" + startHour;
                    } else {
                        strHour += startHour;
                    }

                    if (startMinute == 0) {
                        strMinute = "00";
                    } else {
                        strMinute = "30";
                    }
                    ReservationTime reservationTime = ReservationTime.builder()
                        .date(date2)
                        .time(strHour + ":" + strMinute)
                        .reserved(false)
                        .build();
                    reservationTime.setDesigner(designer);
                    reservationTime.setHairshop(designer.getHairshop());
                    reservationTimeRepository.save(reservationTime);

                    startMinute += 30;
                    if (startMinute >= 60) {
                        startHour += 1;
                        startMinute = 0;
                    }
                }
            }
        }
    }
}