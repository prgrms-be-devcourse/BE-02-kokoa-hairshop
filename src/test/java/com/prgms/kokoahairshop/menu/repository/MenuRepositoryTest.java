package com.prgms.kokoahairshop.menu.repository;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.entity.Gender;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.entity.Type;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MenuRepositoryTest {
    @Autowired
    private HairshopRepository hairshopRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    private Menu menu;

    @BeforeEach
    void setup() {
        User user = User.builder()
            .email("example2@naver.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .auth("USER")
            .build();
        userRepository.save(user);

        Hairshop hairshop = Hairshop.builder()
                .id(1L)
                .name("데브헤어")
                .phoneNumber("010-1234-1234")
                .startTime("11:00")
                .endTime("20:00")
                .closedDay("2")
                .reservationRange("1")
                .reservationStartTime("11:00")
                .reservationEndTime("19:30")
                .sameDayAvailable(true)
                .roadNameNumber("대구 중구 동성로2가 141-9 2층3층")
                .profileImg("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .introduction("시간 여유 충분히 가지고 여유롭게 와주시면 감사하겠습니다 :)")
                .user(user)
                .build();
        hairshop = hairshopRepository.save(hairshop);
        menu = Menu.builder()
                .id(1L)
                .name("커트")
                .price(8000)
                .discount(0)
                .gender(Gender.unisex)
                .type(Type.haircut)
                .image("https://mud-kage.kakao.com/dn/fFVWf/btqFiGBCOe6/LBpRsfUQtqrPHAWMk5DDw0/img_1080x720.jpg")
                .exposedTime(30)
                .hairshop(hairshop)
                .build();
        menu = menuRepository.save(menu);
    }

    @AfterEach
    void tearDown() {
        menuRepository.deleteAll();
        hairshopRepository.deleteAll();
    }

    @Test
    @DisplayName("해당 아이디의 메뉴를 확인 할 수 있다.")
    void FIND_MENU_BY_ID_TEST() {
        // given
        Long id = menu.getId();

        // when
        Optional<Menu> byId = menuRepository.findById(id);

        // then
        assertThat(byId.get().getName()).isEqualTo(menu.getName());
    }

    @Test
    @DisplayName("메뉴 정보를 수정 할 수 있습니다.")
    void UPDATE_MENU_TEST() {
        // given
        menu = menu.toBuilder()
                .price(9000)
                .build();

        // when
        Menu updated = menuRepository.save(menu);

        // then
        Optional<Menu> byId = menuRepository.findById(updated.getId());
        assertThat(byId.get().getPrice()).isEqualTo(updated.getPrice());
    }

    @Test
    @DisplayName("메뉴를 삭제 할 수 있습니다.")
    void DELETE_MENU_TEST() {
        // given
        Long id = menu.getId();

        // when
        menuRepository.deleteById(id);

        // then
        assertThat(menuRepository.findById(id)).isEmpty();
    }
}