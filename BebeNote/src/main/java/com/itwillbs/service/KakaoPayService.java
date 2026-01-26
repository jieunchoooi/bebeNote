package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.itwillbs.config.KakaoPayProperties;
import com.itwillbs.dto.KakaoReadyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {
	
	private final KakaoPayProperties payProperties;
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String auth = "SECRET_KEY " + payProperties.getSecretKey();
		
		log.info("Authorization 헤더: {}", auth);  // 디버깅용
		
		headers.set("Authorization", auth);
		headers.set("Content-Type", "application/json");
		return headers;
	}
	
	// 결제 준비 요청
	public KakaoReadyResponse kakaoPayReady(int amount) {
		log.info("결제 요청 시작 - 금액: {}", amount);
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("cid", payProperties.getCid());
		parameters.put("partner_order_id", "ORDER_" + System.currentTimeMillis()); // ✅ 고유한 주문번호
		parameters.put("partner_user_id", "USER_ID");
		parameters.put("item_name", "베베페이 충전 " + amount + "원");
		parameters.put("quantity", 1);
		parameters.put("total_amount", amount);
		parameters.put("tax_free_amount", 0);  // ✅ 이거 추가!
		
		parameters.put("approval_url", "http://localhost:8080/payment/success");
		parameters.put("fail_url", "http://localhost:8080/payment/fail");
		parameters.put("cancel_url", "http://localhost:8080/payment/cancel");
		
		log.info("요청 파라미터: {}", parameters);  // 디버깅용
		
		HttpEntity<Map<String, Object>> requestEntity = 
			new HttpEntity<>(parameters, this.getHeaders());
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			KakaoReadyResponse kakaoReady = restTemplate.postForObject(
					"https://open-api.kakaopay.com/online/v1/payment/ready", 
					requestEntity,
					KakaoReadyResponse.class);
			
			log.info("카카오페이 응답 성공 - TID: {}", kakaoReady.getTid());
			return kakaoReady;
			
		} catch (Exception e) {
			log.error("카카오페이 요청 실패", e);
			throw e;
		}
	}
}