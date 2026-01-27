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
import com.itwillbs.entity.Member;
import com.itwillbs.entity.Payment;
import com.itwillbs.entity.pay_status;
import com.itwillbs.repository.MemberRepository;
import com.itwillbs.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {
	
	private final KakaoPayProperties payProperties;
	private final PaymentRepository paymentRepository;  
	private final MemberRepository memberRepository;
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String auth = "SECRET_KEY " + payProperties.getSecretKey();
		headers.set("Authorization", auth);
		headers.set("Content-Type", "application/json");
		return headers;
	}
	
	// 결제 준비 요청
	public KakaoReadyResponse kakaoPayReady(int amount, String user_id) {
		log.info("결제 요청 시작 - 금액: {}", amount);
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("cid", payProperties.getCid());
		parameters.put("partner_order_id", "ORDER_" + System.currentTimeMillis());
		parameters.put("partner_user_id", user_id);
		parameters.put("item_name", "베베페이 충전 " + amount + "원");
		parameters.put("quantity", 1);
		parameters.put("total_amount", amount);
		parameters.put("tax_free_amount", 0);
		
		parameters.put("approval_url", "http://localhost:8080/payment/success");
		parameters.put("fail_url", "http://localhost:8080/payment/fail");
		parameters.put("cancel_url", "http://localhost:8080/payment/cancel");
		
		HttpEntity<Map<String, Object>> requestEntity = 
			new HttpEntity<>(parameters, this.getHeaders());
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			KakaoReadyResponse kakaoReady = restTemplate.postForObject(
					"https://open-api.kakaopay.com/online/v1/payment/ready", 
					requestEntity,
					KakaoReadyResponse.class);
			
			log.info("카카오페이 응답 성공 - TID: {}", kakaoReady.getTid());
			
			Member member = memberRepository.findById(user_id)
					.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + user_id));
			
			// DB에 결제 준비 정보 저장
			Payment payment = new Payment();
			payment.setAmount(amount);
			payment.setStatus(pay_status.ready);
			payment.setTid(kakaoReady.getTid());
			payment.setMember(member);
			paymentRepository.save(payment);
			
			log.info("결제 정보 DB 저장 완료 - TID: {}", kakaoReady.getTid());
			
			return kakaoReady;
			
		} catch (Exception e) {
			log.error("카카오페이 요청 실패", e);
			throw e;
		}
	}
	
	// 결제 승인 처리
	public void approvePayment(String tid, String pgToken) {
		Payment payment = paymentRepository.findByTid(tid);
		if (payment != null) {
			payment.setStatus(pay_status.success);
			paymentRepository.save(payment);
			log.info("결제 승인 완료 - TID: {}", tid);
		}
	}
	
	// 결제 실패 처리
	public void failPayment(String tid) {
		Payment payment = paymentRepository.findByTid(tid);
		if (payment != null) {
			payment.setStatus(pay_status.fail);
			paymentRepository.save(payment);
			log.info("결제 실패 처리 - TID: {}", tid);
		}
	}
	
	// 결제 취소 처리
	public void cancelPayment(String tid) {
		Payment payment = paymentRepository.findByTid(tid);
		if (payment != null) {
			payment.setStatus(pay_status.cancel);
			paymentRepository.save(payment);
			log.info("결제 취소 처리 - TID: {}", tid);
		}
	}
}