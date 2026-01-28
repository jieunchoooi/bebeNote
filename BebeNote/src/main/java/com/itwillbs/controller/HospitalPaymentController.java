package com.itwillbs.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.domain.HospitalPaymentVO;
import com.itwillbs.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class HospitalPaymentController {
    
    private final PaymentService paymentService;
    
    /**
     * 병원 진료비 결제 (베베페이)
     */
    @PostMapping("/hospital")
    public ResponseEntity<?> payHospitalFee(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }
            
            Long reservationId = Long.valueOf(body.get("reservationId").toString());
            Integer amount = Integer.valueOf(body.get("amount").toString());
            
            log.info("병원 결제 요청: userId={}, reservationId={}, amount={}", 
                userDetails.getUsername(), reservationId, amount);
            
            HospitalPaymentVO payment = paymentService.payHospitalFee(
                userDetails.getUsername(), reservationId, amount);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "결제가 완료되었습니다.",
                "data", payment
            ));
            
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.error("결제 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("결제 처리 중 오류", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "결제 처리 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 결제 취소
     */
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }
            
            Long paymentId = Long.valueOf(body.get("paymentId").toString());
            
            log.info("결제 취소 요청: userId={}, paymentId={}", 
                userDetails.getUsername(), paymentId);
            
            paymentService.cancelPayment(paymentId, userDetails.getUsername());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "결제가 취소되었습니다."
            ));
            
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.error("결제 취소 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("결제 취소 중 오류", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "결제 취소 중 오류가 발생했습니다."));
        }
    }
    
    /**
     * 로그인 사용자 병원 결제 내역 조회
     */
    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401)
                .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }
        
        List<HospitalPaymentVO> payments = paymentService.getPaymentHistory(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", payments
        ));
    }
}