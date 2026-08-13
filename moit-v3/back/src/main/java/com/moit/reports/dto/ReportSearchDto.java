package com.moit.reports.dto;

import java.time.LocalDate;

import com.moit.reports.enums.ReasonCode;
import com.moit.reports.enums.Status;
import com.moit.reports.enums.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportSearchDto {
	private Status status;			// 상태
	private Character deleteYn;		// 상태 (논리삭제)
	
	private TargetType targetType;	// 대상
	
	private Long memberId;			// 작성자
	private ReasonCode reasonCode;	// 신고사유
	private LocalDate createdAt;	// 날짜
}

// 관리자 검색 기능
// 
// 상태
// PENDING / APPROVED / REJECTED
//
// 상태(논리삭제)
// DELETE
//
// 대상
// MEETUP / REVIEW
//
// 검색
// 작성자 / 신고사유 / 날짜