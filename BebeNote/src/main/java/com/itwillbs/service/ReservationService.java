package com.itwillbs.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.ReservationVO;
import com.itwillbs.entity.Reservation;
import com.itwillbs.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    
    /**
     * 예약 생성
     */
    @Transactional
    public ReservationVO createReservation(String userId, ReservationVO reservationVO) {
        // 중복 예약 체크
        boolean exists = reservationRepository.existsByHospitalNameAndReservationDateAndReservationTime(
            reservationVO.getHospitalName(),
            reservationVO.getReservationDate(),
            reservationVO.getReservationTime()
        );
        
        if (exists) {
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }
        
        // VO -> Entity 변환
        reservationVO.setUserId(userId);
        reservationVO.setStatus("PENDING");
        Reservation reservation = reservationVO.toEntity();
        
        // 저장
        Reservation saved = reservationRepository.save(reservation);
        
        // Entity -> VO 변환 후 반환
        return ReservationVO.fromEntity(saved);
    }
    
    /**
     * 사용자의 모든 예약 조회
     */
    public List<ReservationVO> getUserReservations(String userId) {
        List<Reservation> reservations = reservationRepository
            .findByUserIdOrderByReservationDateDescReservationTimeDesc(userId);
        
        return reservations.stream()
            .map(ReservationVO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * 사용자의 예정된 예약 조회
     */
    public List<ReservationVO> getUpcomingReservations(String userId) {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<Reservation> reservations = reservationRepository
            .findUpcomingReservations(userId, today);
        
        return reservations.stream()
            .map(ReservationVO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * 예약 상세 조회
     */
    public ReservationVO getReservation(String userId, Long reservationId) {
        Reservation reservation = reservationRepository
            .findByReservationIdAndUserId(reservationId, userId)
            .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        
        return ReservationVO.fromEntity(reservation);
    }
    
    /**
     * 예약 취소
     */
    @Transactional
    public void cancelReservation(String userId, Long reservationId) {
        Reservation reservation = reservationRepository
            .findByReservationIdAndUserId(reservationId, userId)
            .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        
        if ("CANCELLED".equals(reservation.getStatus())) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }
        
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }
    
    /**
     * 특정 병원의 특정 날짜 예약된 시간 조회
     */
    public List<String> getReservedTimes(String hospitalName, String date) {
        List<Reservation> reservations = reservationRepository
            .findByHospitalNameAndReservationDate(hospitalName, date);
        
        return reservations.stream()
            .filter(r -> !"CANCELLED".equals(r.getStatus()))
            .map(Reservation::getReservationTime)
            .collect(Collectors.toList());
    }
    
    //나의병원 페이지용
    
    /**
     * 다녀온 병원 조회 (예약 시간 1분 지난 병원들)
     */
    public List<ReservationVO> getVisitedHospitals(String userId) {
        LocalDateTime now = LocalDateTime.now();
        String currentDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        
        List<Reservation> reservations = reservationRepository
            .findPastReservations(userId, currentDate, currentTime);
        
        return reservations.stream()
            .map(ReservationVO::fromEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * 기간별 다녀온 병원 조회
     * @param userId 사용자 ID
     * @param months 조회 기간 (1, 6 등)
     */
    public List<ReservationVO> getVisitedHospitalsByPeriod(String userId, int months) {
        LocalDateTime now = LocalDateTime.now();
        String currentDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        
        // 시작 날짜 계산
        LocalDate startDate = now.minusMonths(months).toLocalDate();
        String startDateStr = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        List<Reservation> reservations = reservationRepository
            .findPastReservationsByPeriod(userId, startDateStr, currentDate, currentTime);
        
        return reservations.stream()
            .map(ReservationVO::fromEntity)
            .collect(Collectors.toList());
    }
}