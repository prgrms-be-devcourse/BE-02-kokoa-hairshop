package com.prgms.kokoahairshop.user.repository;

import com.prgms.kokoahairshop.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByEmail(String email);
}
