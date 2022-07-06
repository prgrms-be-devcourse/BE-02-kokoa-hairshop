package com.prgms.kokoahairshop.reservation.repository;

import com.prgms.kokoahairshop.reservation.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r "
        + "join fetch r.user "
        + "join fetch r.hairshop "
        + "join fetch r.menu "
        + "join fetch r.designer "
        + "where r.user.id = :userId")
    List<Reservation> findReservationsByUserId(@Param("userId") Long userId);

    @Query("select r from Reservation r "
        + "join fetch r.user "
        + "join fetch r.hairshop "
        + "join fetch r.menu "
        + "join fetch r.designer where r.hairshop.id = :hairshopId")
    List<Reservation> findReservationsByHairshopId(@Param("hairshopId") Long hairshopId);

    boolean existsByDateAndTimeAndDesignerId(LocalDate date, String time, Long designerId);
}