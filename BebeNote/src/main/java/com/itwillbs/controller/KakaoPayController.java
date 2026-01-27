package com.itwillbs.controller;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.itwillbs.dto.KakaoReadyResponse;
import com.itwillbs.service.KakaoPayService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;
    
    @PostMapping("/ready")
    @ResponseBody
    public KakaoReadyResponse readyToKakaoPay(
            @RequestBody Map<String, Integer> body,
            HttpSession session) {
        int amount = body.get("amount");
        
        String user_id = SecurityContextHolder.getContext().getAuthentication().getName();
        KakaoReadyResponse response = kakaoPayService.kakaoPayReady(amount,user_id);
        
        // TID를 세션에 저장
        session.setAttribute("tid", response.getTid());
        
        return response;
    }
    
    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("pg_token") String pgToken,
            HttpSession session) {
        
        // 세션에서 TID 가져오기
        String tid = (String) session.getAttribute("tid");
        
        if (tid != null) {
            kakaoPayService.approvePayment(tid, pgToken);
        }
        
        return "redirect:/myPage/payControl?success=true";
    }
    
    @GetMapping("/fail")
    public String paymentFail(HttpSession session) {
        String tid = (String) session.getAttribute("tid");
        if (tid != null) {
            kakaoPayService.failPayment(tid);
        }
        return "redirect:/myPage/payControl?fail=true";
    }
    
    @GetMapping("/cancel")
    public String paymentCancel(HttpSession session) {
        String tid = (String) session.getAttribute("tid");
        if (tid != null) {
            kakaoPayService.cancelPayment(tid);
        }
        return "redirect:/myPage/payControl?cancel=true";
    }
}