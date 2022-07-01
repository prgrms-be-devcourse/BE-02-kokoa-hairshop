package com.prgms.kokoahairshop.reservation1.service;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import com.prgms.kokoahairshop.reservation1.convert.ReservationConverter1;
import com.prgms.kokoahairshop.reservation1.dto.CreateReservationRequestDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationSuccessResponseDto;
import com.prgms.kokoahairshop.reservation1.dto.ReservationTimeResponseDto;
import com.prgms.kokoahairshop.reservation1.entity.Reservations;
import com.prgms.kokoahairshop.reservation1.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation1.exception.DuplicateReservationException;
import com.prgms.kokoahairshop.reservation1.exception.ReservationNotFoundException;
import com.prgms.kokoahairshop.reservation1.repository.ReservationRepository1;
import com.prgms.kokoahairshop.reservation1.repository.ReservationTimeRepository;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
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
public class ReservationService1Impl implements ReservationService1 {

    private final ReservationRepository1 reservationRepository1;
    private final ReservationTimeRepository reservationTimeRepository;
    private final HairshopRepository hairshopRepository;
    private final DesignerRepository designerRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    private final ReservationConverter1 reservationConverter1 = new ReservationConverter1();


    @Override
    @Transactional
    public ReservationSuccessResponseDto saveReservation(CreateReservationRequestDto createReservationRequestDto) {
        ReservationTime reservationTime = reservationTimeRepository.findReservationTimeByDesignerIdAndDateAndTime(
                createReservationRequestDto.getDesignerId(),
                createReservationRequestDto.getDate(),
                createReservationRequestDto.getTime())
            .orElseThrow(() -> new ReservationNotFoundException("예약 가능한 시간이 아닙니다."));
        if (reservationTime.getReserved()) {
            throw new DuplicateReservationException("이미 예약된 시간입니다.");
        }

        Reservations reservations = reservationConverter1.toReservationEntity(
            createReservationRequestDto);

        User user = userRepository.findById(createReservationRequestDto.getUserId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
        Hairshop hairshop = hairshopRepository.findById(createReservationRequestDto.getHairshopId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 헤어샵입니다."));
        Designer designer = designerRepository.findById(createReservationRequestDto.getDesignerId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 디자이너입니다."));
        Menu menu = menuRepository.findById(createReservationRequestDto.getMenuId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 시술 메뉴입니다."));

        reservations.setUser(user);
        reservations.setHairshop(hairshop);
        reservations.setDesigner(designer);
        reservations.setMenu(menu);

        reservationRepository1.save(reservations);

        return new ReservationSuccessResponseDto(reservations.getId());
    }

    //예약자별 예약 조회
    @Override
    public List<ReservationResponseDto> getReservationListByUser(Long userId) {
        return reservationRepository1.findReservationsByUserId(userId)
            .stream()
            .map((reservation) -> reservationConverter1.toReservationResponseDto(reservation))
            .collect(Collectors.toList());
    }

    //헤어샵별 예약 조회
    @Override
    public List<ReservationResponseDto> getReservationListByHairshop(Long hairshopId) {
        return reservationRepository1.findReservationsByHairshopId(hairshopId)
            .stream()
            .map((reservation) -> reservationConverter1.toReservationResponseDto(reservation))
            .collect(Collectors.toList());
    }

    //예약 가능한 시간 검색
    @Override
    public List<ReservationTimeResponseDto> getReservationTimeList(Long hairShopId, LocalDate date) {
        return designerRepository.findDesignerFetchJoinByHairshopIdAndDate(hairShopId, date)
            .stream().map(designer -> reservationConverter1.toReservationTimeResponseDto(designer))
            .collect(Collectors.toList());
    }

    //30일치 ReservationTimes 생성
    @Transactional
    public void createReservationTimes() {
        List<Designer> designers = designerRepository.findAllDesignerFetchJoin();
        LocalDate date = LocalDate.now();
        StringTokenizer st;

        for(int i = 0; i < 30; i++) {
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
                    if(startHour == endHour && startMinute > endMinute) break;
                    String strHour = "";
                    String strMinute;
                    if(startHour < 10) {
                        strHour = "0" + startHour;
                    } else {
                        strHour += startHour;
                    }

                    if(startMinute == 0) {
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
