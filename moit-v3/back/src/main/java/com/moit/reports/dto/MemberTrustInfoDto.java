package com.moit.reports.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberTrustInfoDto {	// 회원 신고 신뢰도/뱃지 조회 DTO

	private Long targetMemberId;	// 신고당한 회원 id
	private String targetNickname;	// 신고당한 회원 닉네임
	
	private Integer trustScore;		// 신뢰도 점수
	private Long reportStatusId;	//	1		/	2		/	3
	private String statusCode;		// 'ACTIVE' / 'WARNING' / 'SUSPENDED'
	private String statusName;		// '정상'		/ '주의'		/ '정지'
									// 클린한 유저 / 선 넘은 어그로 유저 / 진실의 방으로...
}
