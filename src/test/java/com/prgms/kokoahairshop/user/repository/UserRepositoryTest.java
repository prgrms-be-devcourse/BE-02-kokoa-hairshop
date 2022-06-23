package com.prgms.kokoahairshop.user.repository;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.prgms.kokoahairshop.user.entity.User;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    private final User user = new User("example@naver.com", "$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa","USER");

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("email로 user를 조회할 수 있다.")
    void findByEmailTest() {
        userRepository.save(user);
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        assertThat(findUser.isEmpty(), is(false));
        assertThat(findUser.get(), samePropertyValuesAs(user));

    }
}