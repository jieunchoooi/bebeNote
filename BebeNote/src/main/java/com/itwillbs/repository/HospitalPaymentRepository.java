package com.itwillbs.repository;

import com.itwillbs.entity.HospitalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalPaymentRepository extends JpaRepository<HospitalPayment, Long> {
    
    // 사용자의 결제 내역 조회
    List<HospitalPayment> findByUserIdOrderByCreatedAtDesc(String userId);
    
    // 특정 예약의 결제 내역 조회 (중복 결제 방지)
    Optional<HospitalPayment> findByReservationId(Long reservationId);
    
    // 특정 예약에 결제 내역이 있는지 확인
    boolean existsByReservationId(Long reservationId);
}