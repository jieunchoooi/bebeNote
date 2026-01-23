package com.itwillbs.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.dto.KakaoReadyResponse;
import com.itwillbs.service.KakaoPayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

	private final KakaoPayService kakaoPayService;
	
	//결제요청
	@PostMapping("/ready")
	public KakaoReadyResponse readyToKakaoPay() {
		return kakaoPayService.kakaoPayReady();
	}
	
	
}
