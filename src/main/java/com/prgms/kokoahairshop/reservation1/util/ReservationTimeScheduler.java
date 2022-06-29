package com.prgms.kokoahairshop.reservation1.util;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.reservation1.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation1.repository.ReservationTimeRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.StringTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationTimeScheduler {
    // Todo : Scheduler Test Code 작성
    private final ReservationTimeRepository reservationTimeRepository;
    private final DesignerRepository designerRepository;

    @Scheduled(cron = "0 0 0 * * *") // 초, 분, 시, 일, 월 ,요일, 연도(생략가능)
    public void reservationTimeScheduler() {
        LocalDate date = LocalDate.now();
        StringTokenizer st;

        List<Designer> designers = designerRepository.findAllDesignerFetchJoin();
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
                String str;
                if(startMinute == 0) {
                    str = "00";
                } else {
                    str = "30";
                }
                ReservationTime reservationTime = ReservationTime.builder()
                    .date(date)
                    .time(startHour + ":" + str)
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
