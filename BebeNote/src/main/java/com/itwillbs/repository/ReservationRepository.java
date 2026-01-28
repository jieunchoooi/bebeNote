package com.itwillbs.repository;

import com.itwillbs.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // 사용자의 모든 예약 조회
    List<Reservation> findByUserIdOrderByReservationDateDescReservationTimeDesc(String userId);
    
    // 특정 병원의 특정 날짜 예약 조회
    List<Reservation> findByHospitalNameAndReservationDate(String hospitalName, String reservationDate);
    
    // 특정 병원, 날짜, 시간의 예약 존재 여부 확인
    boolean existsByHospitalNameAndReservationDateAndReservationTime(
        String hospitalName, String reservationDate, String reservationTime);
    
    // 사용자의 특정 상태 예약 조회
    List<Reservation> findByUserIdAndStatus(String userId, String status);
    
    // 예약 ID와 사용자 ID로 조회 (본인 예약만 조회)
    Optional<Reservation> findByReservationIdAndUserId(Long reservationId, String userId);
    
    // 특정 날짜 이후의 예약 조회
    @Query("SELECT r FROM Reservation r WHERE r.userId = :userId AND r.reservationDate >= :date ORDER BY r.reservationDate, r.reservationTime")
    List<Reservation> findUpcomingReservations(@Param("userId") String userId, @Param("date") String date);
}