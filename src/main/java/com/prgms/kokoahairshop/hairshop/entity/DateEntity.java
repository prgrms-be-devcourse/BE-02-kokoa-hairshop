package com.prgms.kokoahairshop.hairshop.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class DateEntity {
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @CreatedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
