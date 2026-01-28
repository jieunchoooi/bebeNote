package com.itwillbs.domain;

import com.itwillbs.entity.HospitalPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HospitalPaymentVO {
    private Long paymentId;
    private String userId;
    private Long reservationId;
    private String hospitalName;
    private Integer paymentAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String description;
    private Timestamp createdAt;
    
    // Entity -> VO 변환
    public static HospitalPaymentVO fromEntity(HospitalPayment entity) {
        HospitalPaymentVO vo = new HospitalPaymentVO();
        vo.setPaymentId(entity.getPaymentId());
        vo.setUserId(entity.getUserId());
        vo.setReservationId(entity.getReservationId());
        vo.setHospitalName(entity.getHospitalName());
        vo.setPaymentAmount(entity.getPaymentAmount());
        vo.setPaymentMethod(entity.getPaymentMethod());
        vo.setPaymentStatus(entity.getPaymentStatus());
        vo.setDescription(entity.getDescription());
        vo.setCreatedAt(entity.getCreatedAt());
        return vo;
    }
    
    // VO -> Entity 변환
    public HospitalPayment toEntity() {
        HospitalPayment entity = new HospitalPayment();
        entity.setPaymentId(this.paymentId);
        entity.setUserId(this.userId);
        entity.setReservationId(this.reservationId);
        entity.setHospitalName(this.hospitalName);
        entity.setPaymentAmount(this.paymentAmount);
        entity.setPaymentMethod(this.paymentMethod);
        entity.setPaymentStatus(this.paymentStatus);
        entity.setDescription(this.description);
        return entity;
    }
}
