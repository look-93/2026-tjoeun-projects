package com.moit.reports.entity;

import java.time.LocalDateTime;

import com.moit.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	@Column(name = "MEMBER_ID", nullable = false)
	private Long memberId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "REASON_CODE", length = 20, nullable = false)
	private ReasonCode reasonCode;
	
	@Column(name = "REASON_DETAIL", length = 200, nullable = false)
	private String reasonDetail;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 20, nullable = false)
	private Status status;
	
//	@Column(name = "DELETE_YN", length = 1, nullable = false)
//	private String deleteYn;
	
//	@Column(name = "CREATED_AT", nullable = false)
//	private LocalDateTime createdAt;
//	@Column(name = "UPDATED_AT", nullable = false)
//	private LocalDateTime updatedAt;
//	
//	@PrePersist
//	void onCreate() {
//		this.createdAt = LocalDateTime.now();
//		this.updatedAt = LocalDateTime.now();
//	}
//	@PreUpdate
//	void onUpdate() {
//		this.updatedAt = LocalDateTime.now();
//	}

	public Report(TargetType targetType, Long targetId, Long memberId,
			ReasonCode reasonCode, String reasonDetail) {
		this.targetType = targetType;
		this.targetId = targetId;
		this.memberId = memberId;
		this.reasonCode = reasonCode;
		this.reasonDetail = reasonDetail;
		this.status = Status.PENDING;
//        this.deleteYn = "N";
	}
	
	public void updateReason(ReasonCode reasonCode, String reasonDetail) {
        this.reasonCode = reasonCode;
        this.reasonDetail = reasonDetail;
    }
	
    public void changeStatus(Status status) {this.status = status; }
    
//    public void delete() { this.deleteYn = "Y"; }
	
	//@Column
	//private String memberId;
}