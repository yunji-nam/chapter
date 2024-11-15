package com.example.chapter.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo {
    private Long id;
    private String nickname;
    private String email;

}
