package com.itwillbs.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bookmark")
@Getter
@Setter
@ToString
public class Bookmark {

    // PK: 즐겨찾기 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    // 누가 저장했는지
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    // 병원 정보
    @Column(name = "hospital_name", nullable = false)
    private String hospitalName;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "phone", nullable = true)	
    private String phone;

    @Column(name = "kakao_place_id", nullable = true)
    private String kakaoPlaceId;

    @Column(name = "latitude", nullable = true)
    private Double latitude;

    @Column(name = "longitude", nullable = true)
    private Double longitude;

    @Column(name = "is_bookmarked", nullable = false)
    private boolean isBookmarked;

    // 생성 / 수정 시각 자동 관리
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

}
