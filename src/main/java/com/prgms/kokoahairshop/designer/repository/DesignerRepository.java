package com.prgms.kokoahairshop.designer.repository;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DesignerRepository extends JpaRepository<Designer, Long> {
    @Query("SELECT o FROM Designer AS o WHERE o.hairshop = ?1")
    List<Designer> findByHairshop(Hairshop hairshop);
}
