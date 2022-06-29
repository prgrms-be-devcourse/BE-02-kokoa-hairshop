package com.prgms.kokoahairshop.reservation1.repository;

import com.prgms.kokoahairshop.reservation1.entity.Reservations;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository1 extends JpaRepository<Reservations, Long> {

    @Query("select r from Reservations r "
        + "join fetch r.user "
        + "join fetch r.hairshop "
        + "join fetch r.menu "
        + "join fetch r.designer "
        + "where r.user.id = :userId")
    List<Reservations> findReservationsByUserId(@Param("userId") Long userId);

    @Query("select r from Reservations r "
        + "join fetch r.user "
        + "join fetch r.hairshop "
        + "join fetch r.menu "
        + "join fetch r.designer where r.hairshop.id = :hairshopId")
    List<Reservations> findReservationsByHairshopId(@Param("hairshopId") Long hairshopId);
}
