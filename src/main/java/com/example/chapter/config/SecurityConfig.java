package com.example.chapter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable);
        //form 로그인
        http.formLogin((auth) -> auth.loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/").permitAll());
        //http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);
        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/join", "/css/**", "/images/**",
                        "/kakao/**", "/api/**", "/admin/join", "/books").permitAll()
                .requestMatchers(GET, "/book/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        return http.build();
    }
}
