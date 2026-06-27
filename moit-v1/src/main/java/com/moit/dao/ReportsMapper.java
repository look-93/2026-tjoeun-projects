package com.moit.dao;

import java.util.HashMap;
import java.util.List;

import com.moit.dto.ReportsDto;

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

	
    // ===== admin =====
    public int updateAdmin(ReportsDto dto);
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
