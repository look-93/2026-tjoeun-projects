package com.moit.service;

import java.util.HashMap;
import java.util.List;

import com.moit.dto.ReportsDto;

public interface ReportsService {
	
	// 사용자 본인이 작성한 신고 내역 조회 & 유저 - 페이징
	public List<ReportsDto> selectUserReport( int pstartno, int memberId);
	
	// select id="selectUserCnt" resultType="int"
	public int selectUserCnt(int memberId);
	
	// 사용자 본인이 작성한 신고 내역 상세 조회
	public ReportsDto selectUserReportDetail(ReportsDto dto);
	
	// 신고 작성 기능
	public int insertUserReport(ReportsDto dto);
	
	// 신고 수정 기능 update
	public int updateUserReport(ReportsDto dto);
	
	// 신고 내역 삭제 (update delete_yn = 'Y')
	public int deleteUserReport(ReportsDto dto);
	
	
	
	// ===== admin =====
	// ===== admin =====
	public int updateAdmin(ReportsDto dto);
	
    public int deleteAdmin(int reportId);

    // 관리자 신고 목록 조회 (동적 조건 + 페이징 + 단건 조회까지 포함)
    public List<ReportsDto> selectAdminReports(HashMap<String, Object> map);

    // 관리자 신고 목록 카운트 (동적 조건 반영)
    public int selectAdminReportsCnt(HashMap<String, Object> map);
	
	
/*	// ��ü �Ű� ��� ��� ��ȸ
	public List<ReportsDto> selectAdminReport( HashMap<String, Object> map );
	
	// <select id="selectAdminCnt" resultType="int">
	public int selectAdminCnt();
	
	// �Ű� ��� ������ ��� ��ȸ - MEETUP & REVIEW
	public List<ReportsDto> selectAdminTargetType( HashMap<String, Object> map );

	// �Ű� ��� ������ ��� ��ȸ - PENDING
	public List<ReportsDto> selectAdminStatus( HashMap<String, Object> map );
		
	// �Ű� ��� �� ��ȸ ( delete_yn = 'Y' ���� )
	public ReportsDto selectAdminDetail(int reportId);

	// �Ű� ���� ���� - PENDING(ó�����) - APPROVED(�Ű�Ϸ�)
	public int updateAdmin(ReportsDto dto);

	// �Ű� ���� ���� �� delete
	public int deleteAdmin(int reportId);
	

	// ������ - �Ű� �˻�(�ۼ���)
	public List<ReportsDto> selectAdminMember(int memberId);
	// ������ - �Ű� �˻�(����)
	public List<ReportsDto> selectAdminReason(String reasonCode);
	// ������ - �Ű� �˻�(��¥)
	public List<ReportsDto> selectAdminCreateAt(String createdAt);
*/	
}