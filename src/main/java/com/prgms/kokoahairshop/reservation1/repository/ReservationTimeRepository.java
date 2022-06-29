package com.prgms.kokoahairshop.reservation1.repository;

import com.prgms.kokoahairshop.reservation1.entity.ReservationTime;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationTimeRepository extends JpaRepository<ReservationTime, Long> {

    @Query("select r from ReservationTime r "
        + "where r.designer.id = :designerId and r.date = :date and r.time = :time")
    Optional<ReservationTime> findReservationTimeByDesignerIdAndDateAndTime(@Param("designerId") Long designerId, @Param("date") LocalDate date, @Param("time") String time);

}
