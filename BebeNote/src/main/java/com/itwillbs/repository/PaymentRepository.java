package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Payment;
import com.itwillbs.entity.pay_status;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // tid로 결제 정보 찾기
    Payment findByTid(String tid);
    
    //충전후 합산금액 조회
    List<Payment> findByMember_UserIdAndStatus(String user_id, pay_status status);
    
}