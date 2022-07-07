package com.prgms.kokoahairshop.user.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import com.prgms.kokoahairshop.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    private final User user = User.builder()
        .email("example1@naver.com")
        .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
        .auth("USER")
        .build();

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("email로 user를 조회할 수 있다.")
    void findByEmailTest() {
        userRepository.save(user);
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        assertThat(findUser.isEmpty(), is(false));
        assertThat(findUser.get(), samePropertyValuesAs(user));

    }

}