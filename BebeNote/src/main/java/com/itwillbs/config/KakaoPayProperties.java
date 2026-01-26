package com.itwillbs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "kakao.pay")
@Getter
@Setter
public class KakaoPayProperties {
	
	private String secretKey;
	private String cid;

}
