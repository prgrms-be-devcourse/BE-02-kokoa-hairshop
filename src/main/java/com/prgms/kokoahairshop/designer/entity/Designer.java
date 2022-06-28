package com.prgms.kokoahairshop.designer.entity;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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

    @Builder(toBuilder = true)
    public Designer(Long id, String name, String image, String introduction,
                    Position position, Hairshop hairshop) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.introduction = introduction;
        this.position = position;
        this.hairshop = hairshop;
    }
}
