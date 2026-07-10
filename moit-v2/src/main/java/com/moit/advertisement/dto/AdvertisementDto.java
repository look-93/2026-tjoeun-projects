package com.moit.advertisement.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AdvertisementDto {

    // PK
    private int adId;

    // 기본 정보
    private String title;
    private String content;
    private List<AdvertisementImageDto> imageList = new ArrayList<>();
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
    // 기간 연장 메일 전송시 현재종료시일저장 
    private LocalDateTime extensionRequestEndDatetime;
    private String extensionStatus;

    // 통계
    private int impressions;
    private int clicks;

    // 운영
    private int priorityScore;
    private Long totalBudget;

    private Double fatigueScore;
    
    // 일반 / 프리미엄 구분
    private String adGrade;

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
    
    // 메일용 정보
    private String email;
    private String memberName;
    private long dday;
}
