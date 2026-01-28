package com.itwillbs.entity;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "hospital_payment")
@Getter
@Setter
@ToString
public class HospitalPayment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;
    
    @Column(name = "hospital_name", nullable = false)
    private String hospitalName;
    
    @Column(name = "payment_amount", nullable = false)
    private Integer paymentAmount; // 결제 금액
    
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod = "BEBEPAY"; // 결제 수단
    
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "COMPLETED"; // COMPLETED, CANCELLED, REFUNDED
    
    @Column(name = "description")
    private String description; // 결제 설명
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}