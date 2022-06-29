package com.prgms.kokoahairshop.reservation2.repository;

import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByDateAndTimeAndDesignerId(LocalDate date, String time, Long designerId);
}