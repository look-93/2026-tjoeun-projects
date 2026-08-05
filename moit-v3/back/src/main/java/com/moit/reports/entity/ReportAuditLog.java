package com.moit.reports.entity;

import java.time.LocalDateTime;

import com.moit.member.entity.Member;
import com.moit.util.BaseEntity;
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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "report_audit_logs")
@Getter @Setter
public class ReportAuditLog {
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "report_audit_log_seq_generator")
    @SequenceGenerator( name = "report_audit_log_seq_generator", sequenceName = "report_audit_log_seq", allocationSize = 1)
    @Column(name = "audit_log_id")
    private Long auditLogId;

    // 처리된 신고
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report reportId;

    // 신고를 처리한 관리자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Member adminId;

    // 변경 전 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 20)
    private Status previousStatus;

    // 변경 후 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "changed_status", nullable = false, length = 20)
    private Status changedStatus;

    // 관리자가 입력한 처리 사유
    @Column(name = "process_reason", nullable = false, length = 1000)
    private String processReason;

    // 신고 처리로 변화한 신뢰도 점수
    @Column(name = "trust_score_change")
    private Integer trustScoreChange;
}