package com.prgms.kokoahairshop.user.service;

import com.prgms.kokoahairshop.user.dto.Converter;
import com.prgms.kokoahairshop.user.dto.RegisterUserDto;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.exception.EmailAlreadyExistException;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private Converter converter;

    @Transactional
    public void register(RegisterUserDto registerUserDto){
        // 이미 있는 회원인지 확인
        if (checkExistEmail(registerUserDto.getEmail())){
            throw new EmailAlreadyExistException("User with the same email already exist");
        }
        // 회원가입진행
        User userEntity = converter.registerDtoToEntity(registerUserDto);
        // 비밀번호 암호화
        userEntity.encodePassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userRepository.save(userEntity);
    }

    // 이미 등록된 회원체크
    boolean checkExistEmail(String email){
        return userRepository.findByEmail(email) != null;
    }

}
