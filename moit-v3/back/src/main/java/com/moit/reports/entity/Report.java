package com.moit.reports.entity;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REPORTS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Report extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportId_seq")
	@SequenceGenerator(name = "reportId_seq", sequenceName = "REPORT_SEQ", allocationSize = 1)
	@Column(name = "REPORT_ID")
	private Long reportId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TARGET_TYPE", length = 20, nullable = false)
	private TargetType targetType;
	
	@Column(name = "TARGET_ID", nullable = false)
	private Long targetId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", nullable = false)
	private Member member;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "REASON_CODE", length = 20, nullable = false)
	private ReasonCode reasonCode;
	
	@Column(name = "REASON_DETAIL", length = 200, nullable = false)
	private String reasonDetail;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 20, nullable = false)
	private Status status;
	
	public Report(TargetType targetType, Long targetId, Member member,
			ReasonCode reasonCode, String reasonDetail) {
		this.targetType = targetType;
		this.targetId = targetId;
		this.member = member;
		this.reasonCode = reasonCode;
		this.reasonDetail = reasonDetail;
		this.status = Status.PENDING;
	}
	
	public void updateReason(ReasonCode reasonCode, String reasonDetail) {
        this.reasonCode = reasonCode;
        this.reasonDetail = reasonDetail;
    }
	
    public void changeStatus(Status status) { this.status = status; }
    
	//@Column
	//private String memberId;
}