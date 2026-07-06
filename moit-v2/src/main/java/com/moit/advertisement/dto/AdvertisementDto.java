package com.moit.advertisement.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AdvertisementDto {

    // PK
    private int adId;

    // 기본 정보
    private String title;
    private String content;
    private List<AdvertisementImageDto> imageList;
    private String landingUrl;

    // 유형/노출
    private String adType;       // BANNER / POPUP / VIDEO
    private String position;     // MAIN / SIDE 등

    // 타겟팅
    private Integer targetAgeMin;
    private Integer targetAgeMax;
    private String targetGender;
    private String deviceType;
    private String adChannel;

    // 기간
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    // 상태
    private String status;              // PENDING / OPEN / CLOSED
    private String approvalStatus;      // WAITING / APPROVED / REJECTED

    // 승인 정보
    private Integer approvedBy; // 승인관리자
    private LocalDateTime approvedAt;  // 승인시각
    private String rejectReason; // 반려사유
    private Integer statusUpdatedBy;
    private LocalDateTime statusUpdatedAt;

    // 통계
    private int impressions;
    private int clicks;

    // 운영
    private int priorityScore;
    private Integer totalBudget;

    private Double fatigueScore;

    // AI 검수
    private Double reviewScore;
    private String isSuitable;
    private String reviewMessage;
    private LocalDateTime reviewedAt;

    // 리마인더
    private String reminder30dSent;
    private String reminder14dSent;

    // 관계
    private int advertiserId;

    // 시스템
    private String deleteYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
