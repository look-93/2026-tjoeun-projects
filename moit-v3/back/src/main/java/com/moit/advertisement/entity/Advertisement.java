package com.moit.advertisement.entity;

import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.AdStatus;
import com.moit.advertisement.enums.ApprovalStatus;
import com.moit.advertisement.enums.PaymentStatus;
import com.moit.advertisement.enums.TargetGender;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "advertisements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Integer adId;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "landing_url", length = 500, nullable = false)
    private String landingUrl;

    @Column(name = "target_age_min")
    private Integer targetAgeMin;

    @Column(name = "target_age_max")
    private Integer targetAgeMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_gender", length = 10, nullable = false)
    private TargetGender targetGender;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AdStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", length = 20, nullable = false)
    private ApprovalStatus approvalStatus;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "status_updated_by")
    private Integer statusUpdatedBy;

    @Column(name = "status_updated_at")
    private LocalDateTime statusUpdatedAt;

    @Lob
    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "impressions", nullable = false)
    private Integer impressions;

    @Column(name = "clicks", nullable = false)
    private Integer clicks;

    @Column(name = "priority_score", nullable = false)
    private Integer priorityScore;

    @Column(name = "review_score", precision = 5, scale = 2)
    private BigDecimal reviewScore;

    @Column(name = "is_suitable", length = 1)
    private String isSuitable;

    @Lob
    @Column(name = "review_message")
    private String reviewMessage;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "fatigue_score", precision = 5, scale = 2, nullable = false)
    private BigDecimal fatigueScore;

    @Column(name = "reminder_30d_sent", length = 1, nullable = false)
    private String reminder30dSent;

    @Column(name = "reminder_14d_sent", length = 1, nullable = false)
    private String reminder14dSent;

    @Column(name = "total_budget", precision = 12, scale = 2)
    private BigDecimal totalBudget;

    @Column(name = "advertiser_id", nullable = false)
    private Integer advertiserId;

    @Column(name = "delete_yn", length = 1, nullable = false)
    private String deleteYn;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20, nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "ad_grade", length = 20, nullable = false)
    private AdGrade adGrade;
}