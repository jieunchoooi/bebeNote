package com.itwillbs.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.domain.ReservationVO;
import com.itwillbs.service.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    
    private final ReservationService reservationService;
    
    //예약 페이지 이동
    @GetMapping("/reservation")
    public String reservationPage(Model model, 
                                  @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // 로그인한 경우 예정된 예약 정보 전달
            List<ReservationVO> upcomingReservations = 
                reservationService.getUpcomingReservations(userDetails.getUsername());
            model.addAttribute("upcomingReservations", upcomingReservations);
        }
        return "main/reservation";
    }
    
    //예약 생성 (AJAX)
    @PostMapping("/api/reservations")
    @ResponseBody
    public ResponseEntity<?> createReservation(
            @RequestBody ReservationVO reservationVO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }
            
            ReservationVO result = reservationService.createReservation(
                userDetails.getUsername(), reservationVO);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "예약이 완료되었습니다.",
                "data", result
            ));
            
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("예약 생성 실패", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "예약 처리 중 오류가 발생했습니다."));
        }
    }
    
    // 사용자 예약 목록 조회
    @GetMapping("/api/reservations")
    @ResponseBody
    public ResponseEntity<?> getUserReservations(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401)
                .body(Map.of("success", false, "message", "로그인이 필요합니다."));
        }
        
        List<ReservationVO> reservations = 
            reservationService.getUserReservations(userDetails.getUsername());
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", reservations
        ));
    }
    
    //예약 상세 조회
    @GetMapping("/api/reservations/{reservationId}")
    @ResponseBody
    public ResponseEntity<?> getReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }
            
            ReservationVO reservation = reservationService.getReservation(
                userDetails.getUsername(), reservationId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", reservation
            ));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    
    //특정 병원의 특정 날짜 예약된 시간 조회
    @GetMapping("/api/reservations/reserved-times")
    @ResponseBody
    public ResponseEntity<?> getReservedTimes(
            @RequestParam String hospitalName,
            @RequestParam String date) {
        
        List<String> reservedTimes = reservationService.getReservedTimes(hospitalName, date);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", reservedTimes
        ));
    }
    
    //예약취소
    @DeleteMapping("/api/reservations/{reservationId}")
    @ResponseBody
    public ResponseEntity<?> cancelReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "로그인이 필요합니다."));
            }
            
            reservationService.cancelReservation(userDetails.getUsername(), reservationId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "예약이 취소되었습니다."
            ));
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            log.error("예약 취소 실패", e);
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "예약 취소 중 오류가 발생했습니다."));
        }
    }
}