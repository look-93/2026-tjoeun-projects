package com.moit.reports.dto;

import lombok.Data;

@Data
public class ReportsDto {
	private int reportId;		// 신고 고유 ID
	private String targetType;	// 'MEETUP', 'REVIEW'
	private int targetId;		// 대상 글 고유 ID
	private int memberId;
	
	private String reasonCode;	// 'ABUSE', 'SPAM', 'FAKE_INFO', 'AD', 'NOSHOW', 'ETC'
	private String reasonDetail;// 상세사유
	private String status;		// 상태 ('PENDING', 'REJECTED', 'APPROVED')
	private String deleteYn;	// 삭제 여부
	private String createdAt;
	private String updatedAt;	// 수정일자
	
	// 신고 승인 데이터 기반 신뢰도 점수, 위험뱃지
	private int targetMemberId;	// 신고당한 사람 ID
	private int trustScore;		// 신뢰도점수
	private String badge;		// 뱃지
	
//	private int reportCount;	// �Ű� �Ǽ�
}
