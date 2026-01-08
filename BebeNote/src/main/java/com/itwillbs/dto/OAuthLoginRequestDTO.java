package com.itwillbs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OAuthLoginRequestDTO {

    private String provider; // 로그인 제공자 (naver, kakao) 

    @JsonProperty("socialId")
    private String socialId; // 소셜 서비스 고유 ID 

    private String email; // 이메일

    private String name; // 이름 
    
}
