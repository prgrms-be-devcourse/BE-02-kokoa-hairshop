package com.prgms.kokoahairshop.reservation.entity;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 20, message = "이름을 20자 이하로 작성해주세요.")
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Size(max = 20, message = "휴대폰 번호를 20자 이하로 작성해주세요.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "올바르지 않은 휴대폰 번호입니다.")
    @Column(name = "phoneNumber", nullable = false, length = 20)
    private String phoneNumber;

    @NotNull(message = "날짜를 입력해주세요.")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotBlank(message = "시간을 입력해주세요.")
    @Size(min = 5, max = 5, message = "시간을 5자로 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "시간을 HH:mm으로 입력해주세요.")
    @Column(name = "time", nullable = false, columnDefinition = "char(5)")
    private String time;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Size(min = 5, max = 100, message = "요청사항은 5자 이상 100로 입력해주세요.")
    @Column(name = "request", length = 100)
    private String request;

    @PositiveOrZero(message = "결제 금액은 양수와 0만 가능합니다.")
    @Column(name = "payment_amount", nullable = false)
    private int paymentAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hairshop_id", referencedColumnName = "id")
    private Hairshop hairshop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id", referencedColumnName = "id")
    private Designer designer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @Builder
    public Reservation(Long id, String name, String phoneNumber, LocalDate date, String time,
        ReservationStatus status, String request,
        int paymentAmount, Hairshop hairshop,
        Designer designer, Menu menu, User user) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.time = time;
        this.status = status;
        this.request = request;
        this.paymentAmount = paymentAmount;
        this.hairshop = hairshop;
        this.designer = designer;
        this.menu = menu;
        this.user = user;
    }

    public void changeStatus(ReservationStatus status) {
        this.status = status;
    }
}
