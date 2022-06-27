package com.prgms.kokoahairshop.reservation.repository;

import com.prgms.kokoahairshop.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}