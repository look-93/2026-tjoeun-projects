package com.moit.dto;

import lombok.Data;

@Data
public class AdvertisementDto {
	private int adId;                    // 광고 ID

    private String title;                // 광고 제목
    private String content;              // 광고 내용
    private String imageUrl;             // 이미지 경로
    private String landingUrl;           // 클릭 시 이동 URL

    private String adType;              // BANNER / POPUP / VIDEO
    private String position;            // 노출 위치

    // 타겟팅 (선택 기능)
    private Integer targetAgeMin;
    private Integer targetAgeMax;
    private String targetGender;
    private String deviceType;
    private String adChannel;

    // 기간
    private String startDatetime;
    private String endDatetime;

    // 상태
    private String status;

    // 통계
    private int impressions;
    private int clicks;

    // 운영
    private int priority;
    private Integer totalBudget;

    // 관계
    private int advertiserId;
    private int authorId;

    // 시스템
    private String deleteYn;
    private String createdAt;
    private String updatedAt;
}
