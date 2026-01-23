package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.config.KakaoPayProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {
	
	private final KakaoPayProperties payProperties;
	private RestTemplate restTemplate = new RestTemplate();
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String auth = "SECRET_KEY " + payProperties.getSecretKey();
		headers.set("Authorization", auth);
		headers.set("Content-Type", "application/json");
		return headers;
	}
	
	//결제 완료 요청
	public KakaoReadyResponse kakaoPayReady() {
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("cid", payProperties.getCid());
		parameters.put("partner_order_id", "ORDER_ID"); //실제주문번호
		parameters.put("partner_user_id", "USER_ID"); //실제사용자ID
		parameters.put("item_name", "ITEM_NAME");//실제상품명
		parameters.put("quantity", "1");//수량
		parameters.put("total_amount", "10000");//총금액
		parameters.put("vat_amount", "0");//부가세
		parameters.put("tax_free_amount", "0");//비과세
		parameters.put("approval_url","web에서 등록한 RUL/success");
		parameters.put("fail_url","web에서 등록한 RUL/fail");
		parameters.put("cancel_url","web에서 등록한 RUL/cancel");
		
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters,this.getHeaders());
		
		//외부에보낼 url
		RestTemplate restTemplate = new RestTemplate();
		kakaoReady = restTemplate.postForObject(
				"https://open-api.kakaopay.com/online/v1/payment/ready", 
				requestEntity,
				KakaoReadyResponse.class);
		return kakaoReady;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
