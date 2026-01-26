package com.itwillbs.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.dto.KakaoReadyResponse;
import com.itwillbs.service.KakaoPayService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;
    
    // 결제 준비 (Ajax 요청용)
    @PostMapping("/ready")
    @ResponseBody
    public KakaoReadyResponse readyToKakaoPay(@RequestBody Map<String, Integer> body) {
        int amount = body.get("amount");
        return kakaoPayService.kakaoPayReady(amount);
    }
    
    // 결제 성공 콜백
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("pg_token") String pgToken) {
        // TODO: 결제 승인 처리
        // kakaoPayService.approvePayment(pgToken);
        
        return "redirect:/myPage/payControl?success=true";
    }
    
    // 결제 실패 콜백
    @GetMapping("/fail")
    public String paymentFail() {
        return "redirect:/myPage/payControl?fail=true";
    }
    
    // 결제 취소 콜백
    @GetMapping("/cancel")
    public String paymentCancel() {
        return "redirect:/myPage/payControl?cancel=true";
    }
}