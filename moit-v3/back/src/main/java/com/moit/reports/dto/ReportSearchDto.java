package com.moit.reports.dto;

import java.time.LocalDate;

import com.moit.reports.enums.ReasonCode;
import com.moit.reports.enums.ReportStatus;
import com.moit.reports.enums.TargetType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportSearchDto {
	private String filter;		// 필터 버튼 ALL, MEETUP, REVIEW, PENDING, DELETE
	private String search;		// 검색 종류 MEMBER_NICKNAME, REASON
	private String keyword;		// 검색 키워드 입력받기
	
	@Schema(hidden = true)
	private TargetType targetType;		// 대상
	@Schema(hidden = true)
	private ReportStatus status;		// 상태 (PENDING)
//	private Character deleteYn;			// 상태 (논리삭제)
	@Schema(hidden = true)
    private Character deleteYn = 'N';	// 삭제되지 않은 신고 조회
	
    @Schema(hidden = true)
	private String memberNickname;		// 작성자(닉네임)
    @Schema(hidden = true)
    private ReasonCode reasonCode;		// 신고사유
//	private LocalDate createdAt;		// 날짜
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