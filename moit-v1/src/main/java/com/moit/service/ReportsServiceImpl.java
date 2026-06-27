package com.moit.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.dao.ReportsMapper;
import com.moit.dto.ReportsDto;

@Service
public class ReportsServiceImpl implements ReportsService {
	@Autowired ReportsMapper dao;

	@Override // 사용자 본인이 작성한 신고 내역 조회 & 유저 - 페이징
	public List<ReportsDto> selectUserReport(int pstartno, int memberId) {
		HashMap<String,Object> map = new HashMap<>();
		map.put("start", (pstartno-1)*10);  
		map.put("end"  ,  10);
		map.put("memberId"  ,  memberId);
		
		return dao.selectUserReport(map);
	}

	@Override // select id="selectUserCnt" resultType="int"
	public int selectUserCnt(int memberId) {
		return dao.selectUserCnt(memberId);
	}

	@Override // 사용자 본인이 작성한 신고 내역 상세 조회
	public ReportsDto selectUserReportDetail(ReportsDto dto) {
		return dao.selectUserReportDetail(dto);
	}

	@Override // 신고 작성 기능
	public int insertUserReport(ReportsDto dto) {
		return dao.insertUserReport(dto);
	}

	@Override // 신고 수정 화면 update
	public int updateUserReport(ReportsDto dto) {
		return dao.updateUserReport(dto);
	}

	@Override // 신고 내역 삭제 (update delete_yn = 'Y')
	public int deleteUserReport(ReportsDto dto) {
		return dao.deleteUserReport(dto);
	}

	

	// ===== admin =====
	// ===== admin =====
	@Override
	public int updateAdmin(ReportsDto dto) {
		return dao.updateAdmin(dto);
	}

	@Override
	public int deleteAdmin(int reportId) {
		return dao.deleteAdmin(reportId);
	}

	@Override // 관리자 신고 목록 조회 (동적 조건 + 페이징 + 단건 조회까지 포함)
	public List<ReportsDto> selectAdminReports(HashMap<String, Object> map) {
		return dao.selectAdminReports(map);
	}

	@Override // 관리자 신고 목록 카운트 (동적 조건 반영)
	public int selectAdminReportsCnt(HashMap<String, Object> map) {
		return dao.selectAdminReportsCnt(map);
	}

	/*
	@Override // ��ü �Ű� ��� ��� ��ȸ
	public List<ReportsDto> selectAdminReport(HashMap<String, Object> map) {
		return dao.selectAdminReport(map);
	}

	@Override // <select id="selectAdminCnt" resultType="int">
	public int selectAdminCnt() {
		return dao.selectAdminCnt();
	}

	@Override // �Ű� ��� ������ ��� ��ȸ - MEETUP & REVIEW
	public List<ReportsDto> selectAdminTargetType(HashMap<String, Object> map) {
		return dao.selectAdminTargetType(map);
	}

	@Override // �Ű� ��� ������ ��� ��ȸ - PENDING
	public List<ReportsDto> selectAdminStatus(HashMap<String, Object> map) {
		return dao.selectAdminStatus(map);
	}

	@Override // �Ű� ��� �� ��ȸ ( delete_yn = 'Y' ���� )
	public ReportsDto selectAdminDetail(int reportId) {
		return dao.selectAdminDetail(reportId);
	}

	@Override // �Ű� ���� ���� - PENDING(ó�����) - APPROVED(�Ű�Ϸ�)
	public int updateAdmin(ReportsDto dto) {
		return dao.updateAdmin(dto);
	}

	@Override // �Ű� ���� ���� �� delete
	public int deleteAdmin(int reportId) {
		return dao.deleteAdmin(reportId);
	}

	
	// ===== 통계 =====
	@Override // ������ - �Ű� �˻�(�ۼ���)
	public List<ReportsDto> selectAdminMember(int memberId) {
		return dao.selectAdminMember(memberId);
	}
	@Override // ������ - �Ű� �˻�(����)
	public List<ReportsDto> selectAdminReason(String reasonCode) {
		return dao.selectAdminReason(reasonCode);
	}
	@Override // ������ - �Ű� �˻�(��¥)
	public List<ReportsDto> selectAdminCreateAt(String createdAt) {
		return dao.selectAdminCreateAt(createdAt);
	}
*/
}
