package com.prgms.kokoahairshop.user.service;

import com.prgms.kokoahairshop.jwt.JwtAuthenticationProvider;
import com.prgms.kokoahairshop.user.dto.Converter;
import com.prgms.kokoahairshop.user.dto.LoginUserDto;
import com.prgms.kokoahairshop.user.dto.RegisterUserDto;
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

@Service
@Slf4j
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private Converter converter;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserDetailService(UserRepository userRepository,
        JwtAuthenticationProvider jwtAuthenticationProvider,
        Converter converter) {
        this.userRepository = userRepository;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
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

    // 로그인
    public TokenResponseDto login(LoginUserDto loginUserDto) {
        User user = loadUserByUsername(loginUserDto.getEmail()); // 이메일 검증
        if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
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

        return UserInfoDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .auth(user.getAuth())
            .build();
    }

    // 토큰에서 user객체조회
    // 토큰포함 된 요청후에 호출
    public User getUserFromSecurityContextHolder() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
