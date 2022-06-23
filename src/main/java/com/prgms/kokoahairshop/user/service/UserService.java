package com.prgms.kokoahairshop.user.service;

import com.prgms.kokoahairshop.user.dto.Converter;
import com.prgms.kokoahairshop.user.dto.RegisterUserDto;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.exception.EmailAlreadyExistException;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private Converter converter;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository,
        Converter converter) {
        this.userRepository = userRepository;
        this.converter = converter;
    }


    // 회원가입
    @Transactional
    public Long register(RegisterUserDto registerUserDto) {
        if (checkExistEmail(registerUserDto.getEmail())) {
            throw new EmailAlreadyExistException("Same email already exists");
        }
        // 회원가입진행
        User userEntity = converter.registerDtoToEntity(registerUserDto);
        // 비밀번호 암호화
        log.info(registerUserDto.getPassword());
        userEntity.encodePassword(passwordEncoder.encode(registerUserDto.getPassword()));
        return userRepository.save(userEntity).getId();
    }

    // 회원가입시 회원 이메일 중복체크
    boolean checkExistEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


    // 인증시에 username으로 user조회
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException("Could not found user for this email"));
    }
}
