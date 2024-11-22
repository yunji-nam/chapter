package com.example.chapter.service;

import com.example.chapter.dto.KakaoUserInfo;
import com.example.chapter.entity.User;
import com.example.chapter.entity.UserRoleEnum;
import com.example.chapter.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${kakao.restapi.key}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    public void login(String code) throws JsonProcessingException {
        String accessToken = getToken(code);
        log.info("kakao access token = " + accessToken);
        KakaoUserInfo info = getKakaoUserInfo(accessToken);
        log.info("kakao email = " + info.getEmail());
        User user = registerKakaoUser(info);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, AuthorityUtils.createAuthorityList(user.getRole().name())
        );

        // 인증을 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    private User registerKakaoUser(KakaoUserInfo info) {
        String kakaoEmail = info.getEmail();
        User user = userRepository.findByEmail(kakaoEmail).orElse(null);

        if (user == null) {
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            user = User.builder()
                    .name(info.getNickname())
                    .password(encodedPassword)
                    .email(kakaoEmail)
                    .role(UserRoleEnum.USER)
                    .provider("kakao").build();

            userRepository.save(user);
        }
        return user;
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                        .post(uri)
                        .headers(headers)
                        .body(new LinkedMultiValueMap<>());

        ResponseEntity<JsonNode> response = restTemplate.exchange(requestEntity, JsonNode.class);

        Long id = response.getBody().get("id").asLong();
        String nickname = response.getBody().get("properties").get("nickname").asText();
        String email = response.getBody().get("kakao_account").get("email").asText();

        return new KakaoUserInfo(id, nickname, email);
    }

    private String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        return jsonNode.get("access_token").asText();
    }

}