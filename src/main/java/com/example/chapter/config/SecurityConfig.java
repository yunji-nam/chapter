package com.example.chapter.config;

import com.example.chapter.security.CustomAuthenticationEntryPoint;
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

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


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
                .defaultSuccessUrl("/", true).permitAll());
        //http basic 인증 방식 disable
        http.httpBasic(AbstractHttpConfigurer::disable);
        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/join", "/css/**", "/images/**",
                        "/kakao/**", "/admin/join", "/books", "/api/**").permitAll()
                .requestMatchers(GET, "/book/**").permitAll()
                .requestMatchers(GET, "/books/**").permitAll()
                .requestMatchers(GET, "/search").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }
}
