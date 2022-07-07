package com.prgms.kokoahairshop.user.service;

import com.prgms.kokoahairshop.jwt.JwtAuthenticationProvider;
import com.prgms.kokoahairshop.user.dto.Converter;
import com.prgms.kokoahairshop.user.dto.LoginRequestDto;
import com.prgms.kokoahairshop.user.dto.RegisterRequestDto;
import com.prgms.kokoahairshop.user.dto.RegisterResponseDto;
import com.prgms.kokoahairshop.user.dto.TokenResponseDto;
import com.prgms.kokoahairshop.user.dto.UserInfoDto;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.exception.EmailAlreadyExistException;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Converter converter = new Converter();

    public UserDetailService(UserRepository userRepository,
        JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.userRepository = userRepository;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    // 회원가입
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        // 중복체크
        if (userRepository.existsUserByEmail(registerRequestDto.getEmail())) {
            throw new EmailAlreadyExistException("Same email already exists");
        }
        // 회원가입진행
        User userEntity = converter.registerRequestToEntity(registerRequestDto);
        // 비밀번호 암호화
        log.info(registerRequestDto.getPassword());
        userEntity.encodePassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        return converter.toRegisterResponse(userRepository.save(userEntity).getId());
    }

    // 로그인
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        User user = loadUserByUsername(loginRequestDto.getEmail()); // 이메일 검증
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다."); // 비밀번호 검증
        }
        return TokenResponseDto.builder()
            .accessToken(jwtAuthenticationProvider.createToken(user.getUsername()))
            .build(); // 토큰발급

    }

    // 인증시에 username으로 user조회
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException("Could not found user for this email"));
    }

    // 회원정보조회
    public UserInfoDto userInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return converter.entityToUserInfoDto(user);
    }
}
