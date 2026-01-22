package com.itwillbs.dto;

import lombok.Data;

@Data
public class BookmarkRequest {
    private String hospitalName;
    private String address;
    private String phone;
    private String kakaoPlaceId;
    private Double latitude;
    private Double longitude;
    private boolean isBookmarked;
}
