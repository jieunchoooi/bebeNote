package com.itwillbs.domain;

import java.time.LocalDateTime;
import com.itwillbs.entity.pay_status;

public class PaymentVO {
	
	private Long pay_id;        // 결제 ID
    private int amount;         // 결제 금액
    private pay_status status;  // 결제 상태
    private String tid;         // 카카오페이 TID
    private LocalDateTime created_at; // 생성 시간

}
