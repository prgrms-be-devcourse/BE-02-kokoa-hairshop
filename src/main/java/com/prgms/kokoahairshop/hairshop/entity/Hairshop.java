package com.prgms.kokoahairshop.hairshop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "hairshop")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // https://erjuer.tistory.com/106
public class Hairshop extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Column(name = "closed_day", nullable = true)
    private String closedDay;

    @Column(name = "reservation_range", nullable = false)
    private String reservationRange;

    @Column(name = "reservation_start_time", nullable = false)
    private String reservationStartTime;

    @Column(name = "reservation_end_time", nullable = false)
    private String reservationEndTime;

    @Column(name = "same_day_available", nullable = false)
    private Boolean sameDayAvailable;

    @Column(name = "road_name_number", nullable = false)
    private String roadNameNumber;

    @Column(name = "profile_img", nullable = false)
    private String profileImg;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    // TODO : user 테이블과 연관관계 형성해야함.
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder(toBuilder = true)
    public Hairshop(Long id, String name, String phoneNumber, String startTime, String endTime,
                    String closedDay, String reservationRange, String reservationStartTime,
                    String reservationEndTime, Boolean sameDayAvailable, String roadNameNumber,
                    String profileImg, String introduction, Long userId) {
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
    }
}