package com.itwillbs.entity;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@ToString
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;
    
    // 예약자 정보
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;
    
    // 병원 정보
    @Column(name = "hospital_name", nullable = false)
    private String hospitalName;
    
    @Column(name = "hospital_address")
    private String hospitalAddress;
    
    @Column(name = "hospital_phone")
    private String hospitalPhone;
    
    @Column(name = "kakao_place_id")
    private String kakaoPlaceId;
    
    // 예약 정보
    @Column(name = "reservation_date", nullable = false)
    private String reservationDate; // yyyy-MM-dd 형식
    
    @Column(name = "reservation_time", nullable = false)
    private String reservationTime; // HH:mm 형식
    
    // 예약 상태 (PENDING: 대기, CONFIRMED: 확정, CANCELLED: 취소, COMPLETED: 완료)
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";
    
    // 메모
    @Column(name = "memo", length = 500)
    private String memo;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
}