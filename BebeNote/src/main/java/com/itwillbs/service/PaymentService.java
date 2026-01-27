package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.entity.Payment;
import com.itwillbs.entity.pay_status;
import com.itwillbs.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
	
	private final PaymentRepository paymentRepository;

	//베베페이 잔액조회
	public int getBebepayBalance(String user_id) {
		log.info("getBebepayBalance userId={} ",user_id);
		
		//총 충전금액
		List<Payment> payments = paymentRepository.findByMember_UserIdAndStatus(user_id, pay_status.success);
        
        int total = 0;
        for (Payment payment : payments) {
            total += payment.getAmount();
        }
        
        return total;
	}

}
