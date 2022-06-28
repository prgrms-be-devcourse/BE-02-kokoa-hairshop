package com.prgms.kokoahairshop.hairshop.entity;

import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import com.prgms.kokoahairshop.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Getter
@Table(name = "hairshop")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // https://erjuer.tistory.com/106
public class Hairshop extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 30)
    @Column(name = "name", nullable = false, columnDefinition = "varchar(30)")
    private String name;

    @Size(min = 12, max = 14)
    @Column(name = "phone_number", nullable = false, columnDefinition = "varchar(14)")
    private String phoneNumber;

    // TODO : 오픈시간 마감시간 유효성 검사
    @Size(min = 4, max = 6)
    @Column(name = "start_time", nullable = false, columnDefinition = "char(5)")
    private String startTime;

    @Size(min = 4, max = 6)
    @Column(name = "end_time", nullable = false, columnDefinition = "char(5)")
    private String endTime;

    @Size(min = 1, max = 1)
    @Column(name = "closed_day", nullable = true, columnDefinition = "char(1)")
    private String closedDay;

    @Size(min = 1, max = 1)
    @Column(name = "reservation_range", nullable = false, columnDefinition = "char(1)")
    private String reservationRange;

    @Size(min = 4, max = 6)
    @Column(name = "reservation_start_time", nullable = false, columnDefinition = "char(5)")
    private String reservationStartTime;

    @Size(min = 4, max = 6)
    @Column(name = "reservation_end_time", nullable = false, columnDefinition = "char(5)")
    private String reservationEndTime;

    @Column(name = "same_day_available", nullable = false)
    private Boolean sameDayAvailable;

    @Size(max = 100)
    @Column(name = "road_name_number", nullable = false, columnDefinition = "varchar(100)")
    private String roadNameNumber;

    @Size(max = 200)
    @Column(name = "profile_img", nullable = false, columnDefinition = "varchar(200)")
    private String profileImg;

    @Size(max = 300)
    @Column(name = "introduction", nullable = false, columnDefinition = "varchar(300)")
    private String introduction;

    // TODO : user 테이블과 연관관계 형성해야함.
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "hairshop")
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @Builder(toBuilder = true)
    public Hairshop(Long id, String name, String phoneNumber, String startTime, String endTime,
        String closedDay, String reservationRange, String reservationStartTime,
        String reservationEndTime, Boolean sameDayAvailable, String roadNameNumber,
        String profileImg, String introduction, Long userId, User user) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.closedDay = closedDay;
        this.reservationRange = reservationRange;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
        this.sameDayAvailable = sameDayAvailable;
        this.roadNameNumber = roadNameNumber;
        this.profileImg = profileImg;
        this.introduction = introduction;
        this.userId = userId;
        this.user = user;
    }
}
