package com.moit.advertisement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.AdStatus;
import com.moit.advertisement.enums.ApprovalStatus;
import com.moit.advertisement.enums.PaymentStatus;
import com.moit.advertisement.enums.TargetGender;
// import com.moit.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ADVERTISEMENTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advertisement {

    // 광고 PK
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "advertisement_seq"
    )
    @SequenceGenerator(
        name = "advertisement_seq",
        sequenceName = "ADVERTISEMENT_SEQ",
        allocationSize = 1
    )
    @Column(name = "AD_ID")
    private Long adId;


    // 광고 제목
    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;


    // 광고 내용
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;


    // 광고 클릭 시 이동할 랜딩 URL
    @Column(name = "LANDING_URL", length = 500, nullable = false)
    private String landingUrl;


    // 타겟 최소 연령
    @Column(name = "TARGET_AGE_MIN")
    private Integer targetAgeMin;


    // 타겟 최대 연령
    @Column(name = "TARGET_AGE_MAX")
    private Integer targetAgeMax;


    // 타겟 성별
    // MALE / FEMALE / ALL
    @Enumerated(EnumType.STRING)
    @Column(name = "TARGET_GENDER", length = 10, nullable = false)
    private TargetGender targetGender;


    // 광고 게시 시작일시
    @Column(name = "START_DATETIME", nullable = false)
    private LocalDateTime startDatetime;


    // 광고 게시 종료일시
    @Column(name = "END_DATETIME", nullable = false)
    private LocalDateTime endDatetime;


    // 광고 노출 상태
    // PENDING / OPEN / CLOSED
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    private AdStatus status;


    // 광고 신청 승인 상태
    // WAITING / APPROVED / REJECTED
    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVAL_STATUS", length = 20, nullable = false)
    private ApprovalStatus approvalStatus;


    // 승인한 관리자 회원 PK
    // Member Entity 완성 후 @ManyToOne으로 변경
    /*
    @ManyToOne
    @JoinColumn(name = "APPROVED_BY")
    private Member approvedBy;
    */


    // 광고 승인 일시
    @Column(name = "APPROVED_AT")
    private LocalDateTime approvedAt;


    // 상태를 변경한 관리자 회원 PK
    // Member Entity 완성 후 @ManyToOne으로 변경
    /*
    @ManyToOne
    @JoinColumn(name = "STATUS_UPDATED_BY")
    private Member statusUpdatedBy;
    */


    // 광고 반려 사유
    @Lob
    @Column(name = "REJECT_REASON")
    private String rejectReason;


    // 누적 광고 노출 수
    @Column(name = "IMPRESSIONS", nullable = false)
    private Long impressions;


    // 누적 광고 클릭 수
    @Column(name = "CLICKS", nullable = false)
    private Long clicks;


    // 광고 노출 우선순위
    @Column(name = "PRIORITY_SCORE", nullable = false)
    private Integer priorityScore;


    // AI 광고 검수 점수
    @Column(name = "REVIEW_SCORE", precision = 5, scale = 2)
    private BigDecimal reviewScore;


    // AI 광고 검수 적합 여부
    // Y = 적합 / N = 부적합
    @Column(name = "IS_SUITABLE", length = 1)
    private String isSuitable;


    // AI 광고 검수 결과 및 수정 가이드
    @Lob
    @Column(name = "REVIEW_MESSAGE")
    private String reviewMessage;


    // AI 광고 검수 완료 일시
    @Column(name = "REVIEWED_AT")
    private LocalDateTime reviewedAt;


    // 광고 피로도 점수
    @Column(
        name = "FATIGUE_SCORE",
        precision = 5,
        scale = 2,
        nullable = false
    )
    private BigDecimal fatigueScore;


    // 광고 종료 30일 전 알림 발송 여부
    // Y = 발송 / N = 미발송
    @Column(name = "REMINDER_30D_SENT", length = 1, nullable = false)
    private String reminder30dSent;


    // 광고 종료 14일 전 알림 발송 여부
    // Y = 발송 / N = 미발송
    @Column(name = "REMINDER_14D_SENT", length = 1, nullable = false)
    private String reminder14dSent;


    // 광고 총 예산
    @Column(name = "TOTAL_BUDGET", precision = 12, scale = 2)
    private BigDecimal totalBudget;


    // 광고 신청자(제휴업체) 회원 PK
    // Member Entity 완성 후 @ManyToOne으로 변경
    /*
    @ManyToOne
    @JoinColumn(name = "ADVERTISER_ID", nullable = false)
    private Member advertiser;
    */


    // 광고 Soft Delete 여부
    // Y = 삭제 / N = 정상
    @Column(name = "DELETE_YN", length = 1, nullable = false)
    private String deleteYn;


    // 광고 신청/등록 일시
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;


    // 광고 정보 수정 일시
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;


    // 광고 결제 상태
    // NONE / WAITING / PAID
    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", length = 20, nullable = false)
    private PaymentStatus paymentStatus;


    // 광고 등급
    // GENERAL / PREMIUM
    @Enumerated(EnumType.STRING)
    @Column(name = "AD_GRADE", length = 20, nullable = false)
    private AdGrade adGrade;


    // Entity 최초 저장 시 기본값 설정
    @PrePersist
    void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        // 생성일시 / 수정일시
        this.createdAt = now;
        this.updatedAt = now;

        // 광고 상태 기본값
        if (this.status == null) {
            this.status = AdStatus.PENDING;
        }

        // 승인 상태 기본값
        if (this.approvalStatus == null) {
            this.approvalStatus = ApprovalStatus.WAITING;
        }

        // 결제 상태 기본값
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.NONE;
        }

        // 광고 등급 기본값
        if (this.adGrade == null) {
            this.adGrade = AdGrade.GENERAL;
        }

        // 누적 노출 수 기본값
        if (this.impressions == null) {
            this.impressions = 0L;
        }

        // 누적 클릭 수 기본값
        if (this.clicks == null) {
            this.clicks = 0L;
        }

        // 광고 우선순위 기본값
        if (this.priorityScore == null) {
            this.priorityScore = 5;
        }

        // 광고 피로도 기본값
        if (this.fatigueScore == null) {
            this.fatigueScore = BigDecimal.ZERO;
        }

        // 30일 전 알림 기본값
        if (this.reminder30dSent == null) {
            this.reminder30dSent = "N";
        }

        // 14일 전 알림 기본값
        if (this.reminder14dSent == null) {
            this.reminder14dSent = "N";
        }

        // Soft Delete 기본값
        if (this.deleteYn == null) {
            this.deleteYn = "N";
        }
    }


    // Entity 수정 시 수정일시 자동 갱신
    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}