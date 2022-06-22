package com.prgms.kokoahairshop.user.repository;

import com.prgms.kokoahairshop.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

        User findByEmail(String email);
}
