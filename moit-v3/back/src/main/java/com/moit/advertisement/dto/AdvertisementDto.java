package com.moit.advertisement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.moit.advertisement.entity.Advertisement;
import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.AdStatus;
import com.moit.advertisement.enums.ApprovalStatus;
import com.moit.advertisement.enums.PaymentStatus;
import com.moit.advertisement.enums.TargetGender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AdvertisementDto {

    /**
     * 광고 등록 요청
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdvertisementRequestDto {
    	
    	private Long advertiserId;

        private String title;
        private String content;
        private String landingUrl;

        private Integer targetAgeMin;
        private Integer targetAgeMax;
        private TargetGender targetGender;

        private LocalDateTime startDatetime;
        private LocalDateTime endDatetime;

        private BigDecimal totalBudget;
    }
    
    /**
     * 관리자 광고 업데이트 요청
     *
     * 승인 / 반려 / 상태변경 등에 사용
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdvertisementAdminUpdateDto {

        private Long adId;

        private String status;
        private String approvalStatus;
        private String adGrade;

        private Integer approvedBy;
        private LocalDateTime approvedAt;

        private String rejectReason;

        private Integer statusUpdatedBy;
        private LocalDateTime statusUpdatedAt;
    }

    
    /**
     * 광고 조회 응답
     */
    @Getter
    @Builder
    public static class AdvertisementResponseDto {

        private Long adId;

        private String title;
        private String content;
        private String landingUrl;

        private Integer targetAgeMin;
        private Integer targetAgeMax;
        private TargetGender targetGender;

        private LocalDateTime startDatetime;
        private LocalDateTime endDatetime;

        private AdStatus status;
        private ApprovalStatus approvalStatus;
        private PaymentStatus paymentStatus;
        private AdGrade adGrade;

        private Long advertiserId;

        private Long impressions;
        private Long clicks;

        private Integer priorityScore;

        private BigDecimal totalBudget;

        private BigDecimal fatigueScore;

        // 알림 발송 여부
        private String reminder30dSent;
        private String reminder14dSent;

        // 삭제 여부
        private Character deleteYn;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


        /**
         * Entity → Response DTO
         */
        public static AdvertisementResponseDto fromEntity(
                Advertisement ad
        ) {

            return AdvertisementResponseDto.builder()
                    .adId(ad.getAdId())
                    .title(ad.getTitle())
                    .content(ad.getContent())
                    .landingUrl(ad.getLandingUrl())

                    .targetAgeMin(ad.getTargetAgeMin())
                    .targetAgeMax(ad.getTargetAgeMax())
                    .targetGender(ad.getTargetGender())

                    .startDatetime(ad.getStartDatetime())
                    .endDatetime(ad.getEndDatetime())

                    .status(ad.getStatus())
                    .approvalStatus(ad.getApprovalStatus())
                    .paymentStatus(ad.getPaymentStatus())
                    .adGrade(ad.getAdGrade())

                    .advertiserId(
                            ad.getAdvertiser() != null
                                    ? ad.getAdvertiser().getId()
                                    : null
                    )

                    .impressions(ad.getImpressions())
                    .clicks(ad.getClicks())
                    .priorityScore(ad.getPriorityScore())

                    .totalBudget(ad.getTotalBudget())
                    .fatigueScore(ad.getFatigueScore())
                    
                    .reminder30dSent(ad.getReminder30dSent())
                    .reminder14dSent(ad.getReminder14dSent())

                    .deleteYn(ad.getDeleteYn())

                    .createdAt(ad.getCreatedAt())
                    .updatedAt(ad.getUpdatedAt())

                    .build();
        }
    }
}