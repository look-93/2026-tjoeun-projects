package com.moit.reports.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.moit.reports.entity.Report;
import com.moit.reports.enums.ReasonCode;
import com.moit.reports.enums.ReportStatus;
import com.moit.reports.enums.TargetType;

import lombok.Getter;
import lombok.Setter;

public class ReportsDto {

	// 신고 작성/수정 요청 RequestDto
	@Getter @Setter
	public static class ReportRequestDto {
		private TargetType targetType;
		private Long targetId;
		private ReasonCode reasonCode;
		private String reasonDetail;
	}

	// 신고 응답 ResponseDto
	@Getter @Setter
	public static class ReportResponseDto {
		private Long reportId;
		private TargetType targetType;	// MEETUP/REVIEW
		private Long targetId;			// 모임글번호/리뷰글번호
		private Long memberId;
		private String memberNickname;
		private ReasonCode reasonCode;
		private String reasonDetail;
		private ReportStatus status;
		private Character deleteYn;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		
		// Report Entity -> ReportResponseDto 변환
	    public static ReportResponseDto from(Report report) {
	        ReportResponseDto dto = new ReportResponseDto();
	        dto.setReportId(report.getReportId());
	        dto.setTargetType(report.getTargetType());
	        dto.setTargetId(report.getTargetId());

	        if (report.getMember() != null) {
	            dto.setMemberId(report.getMember().getId());
	            dto.setMemberNickname(report.getMember().getNickname());
	        }
	        dto.setReasonCode(report.getReasonCode());
	        dto.setReasonDetail(report.getReasonDetail());
	        dto.setStatus(report.getStatus());
	        dto.setDeleteYn(report.getDeleteYn());
	        dto.setCreatedAt(report.getCreatedAt());
	        dto.setUpdatedAt(report.getUpdatedAt());
	        return dto;
	    }
	}

	// 관리자 신고 처리 (승인/반려)
	@Getter @Setter
	public static class ReportProcessDto {
		private ReportStatus status; // 상태변경
		private String processReason; // 처리사유
	}

	// 신고 목록 + 페이징 정보 응답 (사용자/관리자)
	@Getter @Setter
	public static class ReportListResponseDto {
		private List<ReportResponseDto> reports;
		private Long totalCount;
		private Long totalPage;
	}
}

//	private Long reportId;		// 신고 고유 ID
//	private TargetType targetType;	// 'MEETUP', 'REVIEW'
//	private Long targetId;		// 대상 글 고유 ID
//	private Long memberId;
//	
//	private ReasonCode reasonCode;	// 'ABUSE', 'SPAM', 'FAKE_INFO', 'AD', 'NOSHOW', 'ETC'
//	private String reasonDetail;// 상세사유
//	private Status status;		// 상태 ('PENDING', 'REJECTED', 'APPROVED')
//	private String deleteYn;	// 삭제 여부
//	private LocalDateTime createdAt;
//	private LocalDateTime updatedAt;	// 수정일자
//	
//	// members 에서 email
//	private String email;
//	
//	// 신고 승인 데이터 기반 신뢰도 점수
//	private int targetMemberId;			// 신고당한 유저
//	private String targetNickname;		// 신고당한 유저 닉네임
//	private int trustScore;				// 신뢰도점수
//	
//	private int approvedCnt;			// 신고 승인(APPROVED) 건수
//	
//	//뱃지 표현
//	private int reportStatusId;		//	1		/	2		/	3
//	private String statusCode;		// 'ACTIVE' / 'WARNING' / 'SUSPENDED'
//	private String statusName;		// '정상'		/ '주의'		/ '정지'
//									// 클린한 유저 / 선 넘은 어그로 유저 / 진실의 방으로...