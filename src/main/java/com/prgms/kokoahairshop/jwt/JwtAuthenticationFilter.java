package com.prgms.kokoahairshop.jwt;

import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.service.UserDetailService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
// 로그인시에 동작하는 필터
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserDetailService userDetailService;

    public JwtAuthenticationFilter(
        JwtAuthenticationProvider jwtAuthenticationProvider,
        UserDetailService userDetailService) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.userDetailService = userDetailService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getToken(request);

            // jwt 토큰이 아니면
            if (token != null) {
                // 토큰으로 유저이름 찾기
                String userName = jwtAuthenticationProvider.getUserNameFromToken(token);

                if (userName != null) {
                    User user = userDetailService.loadUserByUsername(userName);
                    // user로 User
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());

                    // contextholder 에 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            } else {
                log.debug("SecurityContextHolder already have authentication with this token");
            }
        }

        chain.doFilter(request, response);

    }


    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        // jwt 토큰이 아니면
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        return header.replace("Bearer ", "");
    }
}
