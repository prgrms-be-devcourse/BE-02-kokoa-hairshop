package com.prgms.kokoahairshop.designer.repository;

import com.prgms.kokoahairshop.designer.entity.Designer;
import java.time.LocalDate;
import java.util.List;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DesignerRepository extends JpaRepository<Designer, Long> {

    @Query("select d from Designer d join fetch d.reservationTimes r "
        + "where d.hairshop.id = :hairshopId and r.date = :date and r.reserved = false")
    List<Designer> findDesignerFetchJoinByHairshopIdAndDate(@Param("hairshopId") Long hairshopId, @Param("date") LocalDate date);

    @Query("select d from Designer d join fetch d.hairshop h")
    List<Designer> findAllDesignerFetchJoin();

    @Query("SELECT o FROM Designer AS o WHERE o.hairshop = ?1")
    List<Designer> findByHairshop(Hairshop hairshop);

    @Query(value = "SELECT distinct d from Designer d join fetch d.reservations r where d.hairshop.id = :hairshopId and r.date=:date")
    List<Designer> findByHairshopIdAndDate(Long hairshopId, LocalDate date);
}
