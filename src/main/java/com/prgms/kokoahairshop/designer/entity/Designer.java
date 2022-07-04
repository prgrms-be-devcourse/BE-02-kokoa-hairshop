package com.prgms.kokoahairshop.designer.entity;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.reservation1.entity.ReservationTime;
import com.prgms.kokoahairshop.reservation1.entity.Reservations;
import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Table(name = "designer")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // https://erjuer.tistory.com/106
public class Designer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Size(max = 200)
    @Column(name = "profile_img", nullable = false, columnDefinition = "varchar(200)")
    private String image;

    @Size(max = 300)
    @Column(name = "introduction", nullable = false, columnDefinition = "varchar(300)")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false, columnDefinition = "varchar(20)")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hairshop_id", referencedColumnName = "id")
    private Hairshop hairshop;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @CreatedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "designer")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "designer")
    private List<ReservationTime> reservationTimes = new ArrayList<>();

    @Builder(toBuilder = true)
    public Designer(Long id, String name, String image, String introduction,
        Position position, Hairshop hairshop, List<ReservationTime> reservationTimes) {

        this.id = id;
        this.name = name;
        this.image = image;
        this.introduction = introduction;
        this.position = position;
        this.hairshop = hairshop;
        if(reservationTimes != null) {
            this.reservationTimes = reservationTimes;
        }

    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

}
