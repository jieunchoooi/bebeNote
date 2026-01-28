package com.itwillbs.domain;

import java.sql.Timestamp;

import com.itwillbs.entity.Reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReservationVO {
    private Long reservationId;
    private String userId;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPhone;
    private String kakaoPlaceId;
    private String reservationDate; // yyyy-MM-dd
    private String reservationTime; // HH:mm
    private String status;
    private String memo;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Entity -> VO 변환
    public static ReservationVO fromEntity(Reservation entity) {
        ReservationVO vo = new ReservationVO();
        vo.setReservationId(entity.getReservationId());
        vo.setUserId(entity.getUserId());
        vo.setHospitalName(entity.getHospitalName());
        vo.setHospitalAddress(entity.getHospitalAddress());
        vo.setHospitalPhone(entity.getHospitalPhone());
        vo.setKakaoPlaceId(entity.getKakaoPlaceId());
        vo.setReservationDate(entity.getReservationDate());
        vo.setReservationTime(entity.getReservationTime());
        vo.setStatus(entity.getStatus());
        vo.setMemo(entity.getMemo());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setUpdatedAt(entity.getUpdatedAt());
        return vo;
    }
    
    // VO -> Entity 변환
    public Reservation toEntity() {
        Reservation entity = new Reservation();
        entity.setReservationId(this.reservationId);
        entity.setUserId(this.userId);
        entity.setHospitalName(this.hospitalName);
        entity.setHospitalAddress(this.hospitalAddress);
        entity.setHospitalPhone(this.hospitalPhone);
        entity.setKakaoPlaceId(this.kakaoPlaceId);
        entity.setReservationDate(this.reservationDate);
        entity.setReservationTime(this.reservationTime);
        entity.setStatus(this.status);
        entity.setMemo(this.memo);
        return entity;
    }
}