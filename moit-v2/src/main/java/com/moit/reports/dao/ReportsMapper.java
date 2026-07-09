package com.moit.reports.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moit.reports.dto.ReportsDto;

@Mapper
public interface ReportsMapper {

	// ===== user =====
	// ===== user =====
	// 신고 작성  /     서비스에서  - target_type  (MEETUP , REVIEW )
	public int insertUserReport(ReportsDto dto);
	
	// 사용자 본인이 작성한 신고 내역 수정
	public int updateUserReport(ReportsDto dto);

	// 사용자 본인이 작성한 신고 내역 삭제 (update delete_yn = 'Y')
	public int deleteUserReport(ReportsDto dto);

	// 사용자 본인이 작성한 신고 내역 조회 & 유저 - 페이징
	public List<ReportsDto> selectUserReport( HashMap<String, Object> map );

	// select id="selectUserCnt" resultType="int"
	public int selectUserCnt(int memberId);
	
	// 사용자 본인이 작성한 신고 내역 상세 조회
	public ReportsDto selectUserReportDetail(ReportsDto dto);
	
	//	중복 신고 방지
	public int doubleReport(ReportsDto dto);
	// 신고 난사 방지
	public int TodayReport(ReportsDto dto);
	
	
	
    // ===== admin =====
	// 신고당한 글 작성자 조회
	public int selectTargetMemberId(ReportsDto dto);
	// 신고당한 글 작성자 닉네임 조회
	public String selectNickname(ReportsDto dto);
	
	// APPROVED 승인 건수 조회 (MEETUP + REVIEW)
	public int approvedCnt(ReportsDto dto);
	
	// 신뢰도 점수 select
	// 뱃지 select
	
	// 신뢰도 점수 update
	public int updateTrustScore(ReportsDto dto);
	// 뱃지 1/2/3 update
	public int updateBadge(ReportsDto dto);
	
    public int updateAdmin(ReportsDto dto);
    public String selectEmail(ReportsDto dto); 
    public int deleteAdmin(int reportId);

    // 관리자 신고 목록 조회 (동적 조건 + 페이징 + 단건 조회까지 포함)
    public List<ReportsDto> selectAdminReports(HashMap<String, Object> map);

    // 관리자 신고 목록 카운트 (동적 조건 반영)
    public int selectAdminReportsCnt(HashMap<String, Object> map);

    // ===== 통계 =====
    public List<ReportsDto> selectReasonReportCount();
    public List<ReportsDto> selectMemberReportCount();
    public List<ReportsDto> selectTargetReportCount();
	
}
