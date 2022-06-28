package com.prgms.kokoahairshop.menu.repository;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT o FROM Menu AS o WHERE o.hairshop = ?1")
    List<Menu> findByHairshop(Hairshop hairshop);
}
