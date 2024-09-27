package com.example.chapter.filter;

import com.example.chapter.security.UserDetailsServiceImpl;
import com.example.chapter.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // 헤더 검증
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
            log.info("token null");
            filterChain.doFilter(request, response); // 다음 필터(Authentication filter)로 이동
            return;
        }

        String token = authorization.split(" ")[1];
        if (!jwtUtil.validateToken(token)) {
            log.error("토큰 유효성 검사 실패");
            filterChain.doFilter(request, response);
            return;
        }

        Claims userInfo = jwtUtil.getUserInfoFromToken(token);
        try {
            setAuthentication(userInfo.get("username").toString());
        } catch (Exception e) {
            response.setStatus(403);
            log.error("인증에 실패했습니다.: " + e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);

    }

    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
