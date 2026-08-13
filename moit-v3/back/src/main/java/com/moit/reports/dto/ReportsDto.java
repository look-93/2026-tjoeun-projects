package com.moit.reports.dto;

import java.time.LocalDateTime;

import com.moit.reports.entity.ReasonCode;
import com.moit.reports.entity.Status;
import com.moit.reports.entity.TargetType;

import lombok.Data;

@Data
public class ReportsDto {
	private Long reportId;		// 신고 고유 ID
	private TargetType targetType;	// 'MEETUP', 'REVIEW'
	private Long targetId;		// 대상 글 고유 ID
	private Long memberId;
	
	private ReasonCode reasonCode;	// 'ABUSE', 'SPAM', 'FAKE_INFO', 'AD', 'NOSHOW', 'ETC'
	private String reasonDetail;// 상세사유
	private Status status;		// 상태 ('PENDING', 'REJECTED', 'APPROVED')
	private String deleteYn;	// 삭제 여부
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;	// 수정일자
	
	// members 에서 email
	private String email;
	
	// 신고 승인 데이터 기반 신뢰도 점수
	private int targetMemberId;			// 신고당한 유저
	private String targetNickname;		// 신고당한 유저 닉네임
	private int trustScore;				// 신뢰도점수
	
	private int approvedCnt;			// 신고 승인(APPROVED) 건수
	
	//뱃지 표현
	private int reportStatusId;		//	1		/	2		/	3
	private String statusCode;		// 'ACTIVE' / 'WARNING' / 'SUSPENDED'
	private String statusName;		// '정상'		/ '주의'		/ '정지'
									// 클린한 유저 / 선 넘은 어그로 유저 / 진실의 방으로...
	
//	private int reportCount;	// �Ű� �Ǽ�
}