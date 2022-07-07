package com.prgms.kokoahairshop.reservation.entity;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_time", indexes = @Index(name = "i_reservationTime", columnList = "date, reserved"))
@Getter
@NoArgsConstructor
public class ReservationTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false, columnDefinition = "char(5)")
    private String time;

    @Column(name = "reserved", nullable = false)
    private Boolean reserved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id", referencedColumnName = "id")
    Designer designer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hairshop_id", referencedColumnName = "id")
    Hairshop hairshop;

    @Builder
    public ReservationTime(Long id, LocalDate date, String time, boolean reserved, Designer designer, Hairshop hairshop) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.reserved = reserved;
        this.designer = designer;
        this.hairshop = hairshop;
    }

    public void setDesigner(Designer designer) {
        if(this.designer != null) {
            this.designer.getReservationTimes().remove(this);
        }
        this.designer = designer;
        designer.getReservationTimes().add(this);
    }

    public void setHairshop(Hairshop hairshop) {
        this.hairshop = hairshop;
    }

    public void changeReserved() {
        this.reserved = true;
    }


}
