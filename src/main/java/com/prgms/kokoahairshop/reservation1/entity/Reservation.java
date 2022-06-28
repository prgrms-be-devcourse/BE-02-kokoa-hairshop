package com.prgms.kokoahairshop.reservation1.entity;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.user.entity.User;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "phoneNumber", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false, columnDefinition = "char(5)")
    String time;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name = "request")
    private String request;

    @Column(name = "payment_amount", nullable = false)
    private int paymentAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false, referencedColumnName = "id")
    Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id", nullable = false, referencedColumnName = "id")
    Designer designer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hairshop_id", nullable = false, referencedColumnName = "id")
    Hairshop hairshop;

    @Builder
    public Reservation(Long id, String name, String phoneNumber, LocalDate date,
        String time, ReservationStatus status, String request,
        int paymentAmount, User user, Menu menu, Designer designer, Hairshop hairshop) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.time = time;
        this.status = status;
        this.request = request;
        this.paymentAmount = paymentAmount;
        this.user = user;
        this.menu = menu;
        this.designer = designer;
        this.hairshop = hairshop;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }

    public void setHairshop(Hairshop hairshop) {
        this.hairshop = hairshop;
    }
}
