package com.prgms.kokoahairshop.designer.repository;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DesignerRepositoryTest {
    @Autowired
    private DesignerRepository designerRepository;

    @Autowired
    private HairshopRepository hairshopRepository;

    private Designer designer;

    private Hairshop hairshop;

    @BeforeAll
    void setup() {
        hairshopRepository.deleteAll();
        hairshop = Hairshop.builder()
                .id(1L)
                .name("데브헤어")
                .phoneNumber("010-1234-1234")
                .startTime("11:00")
                .endTime("20:00")
                .closedDay("화")
                .reservationRange("1")
                .reservationStartTime("11:00")
                .reservationEndTime("19:30")
                .sameDayAvailable(true)
                .roadNameNumber("대구 중구 동성로2가 141-9 2층3층")
                .profileImg("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .introduction("시간 여유 충분히 가지고 여유롭게 와주시면 감사하겠습니다 :)")
                .userId(1L)
                .build();
        hairshop = hairshopRepository.save(hairshop);

        designerRepository.deleteAll();
        designer = Designer.builder()
                .id(1L)
                .name("나그맨")
                .image("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpgㅍ")
                .introduction("안녕하세요.")
                .position(Position.DESIGNER)
                .hairshop(hairshop)
                .build();
        designer = designerRepository.save(designer);
    }

    @Test
    @DisplayName("해당 아이디의 디자이너를 확인 할 수 있다.")
    void FIND_DESIGNER_BY_ID_TEST() {
        // given
        Long id = designer.getId();

        // when
        Optional<Designer> byId = designerRepository.findById(id);

        // then
        assertThat(byId.get().getName()).isEqualTo(designer.getName());
    }

    @Test
    @DisplayName("디자이너 정보를 수정 할 수 있습니다.")
    void UPDATE_DESIGNER_TEST() {
        // given
        designer = designer.toBuilder()
                .position(Position.MANAGER)
                .build();

        // when
        Designer updated = designerRepository.save(designer);

        // then
        Optional<Designer> byId = designerRepository.findById(updated.getId());
        assertThat(byId.get().getPosition()).isEqualTo(updated.getPosition());
    }

    @Test
    @DisplayName("디자이너를 삭제 할 수 있습니다.")
    void DELETE_DESIGNER_TEST() {
        // given
        Long id = designer.getId();

        // when
        designerRepository.deleteById(id);

        // then
        assertThat(designerRepository.findById(id)).isEmpty();
    }
}