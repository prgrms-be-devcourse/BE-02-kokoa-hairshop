package com.prgms.kokoahairshop.menu.entity;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.reservation2.entity.Reservation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "hairshop")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // https://erjuer.tistory.com/106
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 30)
    @Column(name = "name", nullable = false, columnDefinition = "varchar(30)")
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "discount", nullable = false)
    private Integer discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, columnDefinition = "varchar(10)")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20)")
    private Type type;

    @Column(name = "exposed_time", nullable = false)
    private Integer exposedTime;

    @Size(max = 200)
    @Column(name = "image", nullable = true, columnDefinition = "varchar(200)")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hairshop_id", referencedColumnName = "id")
    private Hairshop hairshop;

    @OneToMany(mappedBy = "menu")
    private List<Reservation> reservations = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @CreatedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder(toBuilder = true)
    public Menu(Long id, String name, Integer price, Integer discount, Gender gender,
                Type type, Integer exposedTime, String image, Hairshop hairshop) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.gender = gender;
        this.type = type;
        this.exposedTime = exposedTime;
        this.image = image;
        this.hairshop = hairshop;
    }
}
