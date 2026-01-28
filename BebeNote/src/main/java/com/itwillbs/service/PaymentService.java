package com.itwillbs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.entity.HospitalPayment;
import com.itwillbs.entity.Payment;
import com.itwillbs.entity.Reservation;
import com.itwillbs.entity.pay_status;
import com.itwillbs.repository.HospitalPaymentRepository;
import com.itwillbs.repository.PaymentRepository;
import com.itwillbs.repository.ReservationRepository;
import com.itwillbs.domain.HospitalPaymentVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentService {
	
	private final PaymentRepository paymentRepository;
	private final HospitalPaymentRepository hospitalPaymentRepository;
	private final ReservationRepository reservationRepository;
	
	// ===== 베베페이 잔액 관리 =====
	
	/**
	 * 베베페이 잔액 조회
	 */
	public int getBebepayBalance(String user_id) {
		log.info("getBebepayBalance userId={}", user_id);
		
		// 1. 총 충전 금액
		List<Payment> payments = paymentRepository.findByMember_UserIdAndStatus(user_id, pay_status.success);
        
        int totalCharged = 0;
        for (Payment payment : payments) {
            totalCharged += payment.getAmount();
        }
        
        // 2. 총 사용 금액 (병원 결제)
        List<HospitalPayment> hospitalPayments = hospitalPaymentRepository.findByUserIdOrderByCreatedAtDesc(user_id);
        
        int totalUsed = 0;
        for (HospitalPayment hp : hospitalPayments) {
            if ("COMPLETED".equals(hp.getPaymentStatus())) {
                totalUsed += hp.getPaymentAmount();
            }
        }
        
        // 3. 잔액 = 충전금액 - 사용금액
        int balance = totalCharged - totalUsed;
        
        log.info("충전: {}, 사용: {}, 잔액: {}", totalCharged, totalUsed, balance);
        
        return balance;
	}
	
	// ===== 병원 진료비 결제 =====
	
	/**
	 * 베베페이로 병원 진료비 결제
	 */
	@Transactional
	public HospitalPaymentVO payHospitalFee(String userId, Long reservationId, int amount) {
		log.info("payHospitalFee userId={}, reservationId={}, amount={}", userId, reservationId, amount);
		
		// 1. 예약 정보 확인
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
		
		// 2. 본인 예약인지 확인
		if (!reservation.getUserId().equals(userId)) {
			throw new IllegalArgumentException("본인의 예약만 결제할 수 있습니다.");
		}
		
		// 3. 중복 결제 확인
		if (hospitalPaymentRepository.existsByReservationId(reservationId)) {
			throw new IllegalStateException("이미 결제된 예약입니다.");
		}
		
		// 4. 베베페이 잔액 확인
		int balance = getBebepayBalance(userId);
		
		if (balance < amount) {
			throw new IllegalStateException("베베페이 잔액이 부족합니다. (잔액: " + balance + "원)");
		}
		
		// 5. 병원 결제 기록 생성
		HospitalPayment payment = new HospitalPayment();
		payment.setUserId(userId);
		payment.setReservationId(reservationId);
		payment.setHospitalName(reservation.getHospitalName());
		payment.setPaymentAmount(amount);
		payment.setPaymentMethod("BEBEPAY");
		payment.setPaymentStatus("COMPLETED");
		payment.setDescription(reservation.getHospitalName() + " 진료비 결제");
		
		HospitalPayment saved = hospitalPaymentRepository.save(payment);
		
		log.info("병원 결제 완료: paymentId={}", saved.getPaymentId());
		
		return HospitalPaymentVO.fromEntity(saved);
	}
	
	/**
	 * 결제 취소
	 */
	@Transactional
	public void cancelPayment(Long paymentId, String userId) {
		log.info("cancelPayment paymentId={}, userId={}", paymentId, userId);
		
		HospitalPayment payment = hospitalPaymentRepository.findById(paymentId)
			.orElseThrow(() -> new IllegalArgumentException("결제 내역을 찾을 수 없습니다."));
		
		// 본인 결제인지 확인
		if (!payment.getUserId().equals(userId)) {
			throw new IllegalArgumentException("본인의 결제만 취소할 수 있습니다.");
		}
		
		// 이미 취소된 결제인지 확인
		if ("CANCELLED".equals(payment.getPaymentStatus()) || "REFUNDED".equals(payment.getPaymentStatus())) {
			throw new IllegalStateException("이미 취소된 결제입니다.");
		}
		
		// 결제 취소 처리
		payment.setPaymentStatus("CANCELLED");
		hospitalPaymentRepository.save(payment);
		
		log.info("결제 취소 완료: paymentId={}", paymentId);
	}
	
	// ===== 결제 내역 조회 =====
	
	/**
	 * 사용자의 병원 결제 내역 조회
	 */
	public List<HospitalPaymentVO> getPaymentHistory(String userId) {
		log.info("getPaymentHistory userId={}", userId);
		
		List<HospitalPayment> payments = hospitalPaymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
		
		return payments.stream()
			.map(HospitalPaymentVO::fromEntity)
			.collect(Collectors.toList());
	}
	
	/**
	 * 특정 예약의 결제 여부 확인
	 */
	public boolean isReservationPaid(Long reservationId) {
		return hospitalPaymentRepository.existsByReservationId(reservationId);
	}
}