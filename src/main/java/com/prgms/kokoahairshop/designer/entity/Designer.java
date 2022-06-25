package com.prgms.kokoahairshop.designer.entity;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Table(name = "designer")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // https://erjuer.tistory.com/106
public class Designer extends DateEntity {
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

    // TODO : Attribute Converter 사용고려 -> https://galid1.tistory.com/572
    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false, columnDefinition = "ENUM('원장', '실장', '디자이너')")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hairshop_id", referencedColumnName = "id")
    private Hairshop hairshop;

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
