package com.itwillbs.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.dto.OAuthLoginRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

	public String getAccessToken(String code) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", clientId);
		params.add("redirect_uri", redirectUri);
		params.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

			ResponseEntity<Map> response = restTemplate.postForEntity(
					"https://kauth.kakao.com/oauth/token", request, Map.class);
			String accessToken = (String) response.getBody().get("access_token");
			return accessToken;
	}

	public OAuthLoginRequestDTO getUserInfo(String accessToken) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<?> request = new HttpEntity<>(headers);

			ResponseEntity<Map> response = restTemplate.exchange(
					"https://kapi.kakao.com/v2/user/me", 
					HttpMethod.GET, 
					request, 
					Map.class);
			
			Map<String, Object> body = response.getBody();
			Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
			Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

			OAuthLoginRequestDTO dto = new OAuthLoginRequestDTO();
			dto.setProvider("KAKAO");
			dto.setSocialId(String.valueOf(body.get("id")));
			dto.setEmail((String) kakaoAccount.get("email"));
			dto.setName((String) profile.get("nickname"));
			
			return dto;
			
	}
}