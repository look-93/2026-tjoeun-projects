package com.moit.reports.entity;

import java.time.LocalDateTime;

import com.moit.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REPORT_AUDIT_LOGS")	// 신고 처리 이력
@Getter @Setter @NoArgsConstructor
public class ReportAuditLog {
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "report_audit_log_seq_generator")
    @SequenceGenerator( name = "report_audit_log_seq_generator", sequenceName = "REPORT_AUDIT_LOG_SEQ", allocationSize = 1)
    @Column(name = "AUDIT_LOG_ID")
    private Long auditLogId;

    // 처리된 신고
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_ID", nullable = false)
    private Report report;

    // 신고를 처리한 관리자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ID", nullable = false)
    private Member admin;

    // 변경 전 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "PREVIOUS_STATUS", nullable = false, length = 20)
    private Status previousStatus;
    // 변경 후 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "CHANGED_STATUS", nullable = false, length = 20)
    private Status changedStatus;

    // 관리자가 입력한 처리 사유
    @Column(name = "PROCESS_REASON", nullable = false, length = 1000)
    private String processReason;
    // 관리자 처리 시각
    @Column(name = "PROCESSED_AT", nullable = false, updatable = false)
    private LocalDateTime processedAt;
    
    @PrePersist
    void onProcess() {
    	this.processedAt = LocalDateTime.now();
    }

    // 변화한 신뢰도 점수
    @Column(name = "TRUST_SCORE_CHANGE", nullable = false)
    private Integer trustScoreChange;

	public ReportAuditLog(Report report, Member admin, Status previousStatus,
			Status changedStatus, String processReason, Integer trustScoreChange) {
		this.report = report;
		this.admin = admin;
		this.previousStatus = previousStatus;
		this.changedStatus = changedStatus;
		this.processReason = processReason;
		this.trustScoreChange = trustScoreChange;
	}
}