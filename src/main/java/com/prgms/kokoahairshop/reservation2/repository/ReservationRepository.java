package com.prgms.kokoahairshop.reservation2.repository;

import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByTimeAndDesignerId(String time, Long designerId);
}