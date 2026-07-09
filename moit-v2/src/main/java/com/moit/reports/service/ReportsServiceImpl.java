package com.moit.reports.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.reports.api.ApiEmail;
import com.moit.reports.dao.ReportsMapper;
import com.moit.reports.dto.ReportsDto;

import javassist.compiler.ast.Keyword;

@Service
public class ReportsServiceImpl implements ReportsService {
	@Autowired ReportsMapper dao;
	@Autowired ApiEmail apiEmail;
	@Autowired ReportsMapper mapper;

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

		// 중복 신고 select count(*) 쿼리 호출
		int count = dao.doubleReport(dto);
		// 이미 신고가 존재하면 insert를 하지 않고 -1(또는 특정 에러코드) 반환
//		if (count > 0) { return -1; }

		// 신고 난사 select count(*) 쿼리 호출
		int todayCnt = dao.TodayReport(dto);
		// 5회 이상 신고하면 insert를 하지 않고 -2 반환
//		if (todayCnt >= 5) { return -2; }
		
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
		
		// rejected or approved
		int result = dao.updateAdmin(dto);
		
		if( "APPROVED".equals(dto.getStatus()) ) {	// status가 APPROVED라면
			int targetMemberId = dao.selectTargetMemberId(dto);
			dto.setTargetMemberId(targetMemberId);	// 신고당한 대상 아이디(정보) 불러오기
			
			int approvedCnt = dao.selectApprovedCnt(targetMemberId);
			int noshowCnt = dao.selectNoshowCnt(targetMemberId);
			int reportCnt = dao.selectReportCnt(targetMemberId);
			
			int trustScore = 100 + (approvedCnt * 2) - (noshowCnt * 10) - (reportCnt * 5);
			int reportStatusId = 1; // 1=정상 2=주의 3=정지
			
			if( trustScore >= 80 ) {
				reportStatusId = 1;	
			} else if ( trustScore >= 40 ) {
				reportStatusId = 2;
			} else {
				reportStatusId = 3;
			}
			
			ReportsDto updateDto = new ReportsDto();
			updateDto.setMemberId(targetMemberId);
			updateDto.setTrustScore(trustScore);
			updateDto.setReportStatusId(reportStatusId);
			
			dao.updateMemberTrustScore(updateDto);		// 신뢰도 점수 update
			dao.updateMemberReportStatusId(updateDto);	// 뱃지 상태 update
		}
		
		
		///////////////////////////////////////////////////
		// apiEmail content
		String content = "신고 처리되지 않음.";
		if( "APPROVED".equals(dto.getStatus()) ) {
			content = "신고 처리가 승인 되었습니다.";
		} else if( "REJECTED".equals(dto.getStatus()) ) {
			content = "신고 처리가 반려 되었습니다.";
		}
		
		// apiEmail Email
		String email = dao.selectEmail(dto);
		
		apiEmail.sendMail(content, email); //메일 test
		return result;
	}
	
	@Override
	public int deleteAdmin(int reportId) {
		
		ReportsDto dto = new ReportsDto();
		dto.setReportId(reportId);

		// apiEmail Email
		String email = dao.selectEmail(dto);
		
		// apiEmail content
		String content="삭제되지 않음.";
		int result = dao.deleteAdmin(reportId);

		if( result > 0 ) {
			content = "신고 글이 삭제 되었습니다.";
			
			if( email != null ) {
				apiEmail.sendMail(content, email); //메일 test

			} else { System.out.println("메일 전송 실패..."); }
		}
		
		return result;
	}
	
	
	@Override
	public ReportsDto findMemberTrustInfo(ReportsDto dto) {
		return dao.findMemberTrustInfo(dto);
	}

	@Override // 관리자 신고 목록 조회 (동적 조건 + 페이징 + 단건 조회까지 포함)
	public List<ReportsDto> selectAdminReports(HashMap<String, Object> map) {
		
//		List<ReportsDto> list = dao.selectAdminReports(map);
//		
//		for (ReportsDto dto : list) {
//			// 신고당한 유저
//			int targetMemberId = dao.selectTargetMemberId(dto);
//			dto.setTargetMemberId(targetMemberId);
//			
//			// 닉네임
//			String targetNickname = dao.selectNickname(dto);
//			dto.setTargetNickname(targetNickname);
//			
//			// 신뢰도 점수
//			int trustScore = dao.selectTrustScore(dto);
//			dto.setSelectTrustScore(trustScore);
//			
//			// 뱃지
//			String StatusName = dao.selectBadge(dto);
//			dto.setReportStatusName(StatusName);
//		}
		
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
