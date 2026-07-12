package com.moit.reports.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.reports.api.ApiEmail;
import com.moit.reports.dao.ReportsMapper;
import com.moit.reports.dto.ReportsDto;

@Service
public class ReportsServiceImpl implements ReportsService {
	@Autowired ReportsMapper dao;
	@Autowired ApiEmail apiEmail;

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
		if (count > 0) { return -1; }

		// 신고 난사 select count(*) 쿼리 호출
		int todayCnt = dao.TodayReport(dto);
		// 5회 이상 신고하면 insert를 하지 않고 -2 반환
		if (todayCnt >= 5) { return -2; }
		
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
			int targetMemberId = dao.selectTargetMemberId(dto); // 신고당한 대상 아이디(정보) 불러오기
			dto.setTargetMemberId(targetMemberId);	
			
			// 신뢰도 점수 sql 쿼리 3개
//			int approvedCnt = dao.selectApprovedCnt(targetMemberId);
//			int noshowCnt = dao.selectNoshowCnt(targetMemberId);
//			int reportCnt = dao.selectReportCnt(targetMemberId);
			// 계산
//			int trustScore = 100 + (approvedCnt * 2) - (noshowCnt * 10) - (reportCnt * 5);

			// 신뢰도 점수 sql 쿼리 1개
			int calTrustScore = dao.calTrustScore(targetMemberId);

			int reportStatusId = 1;
			if( calTrustScore >= 80 ) {
				reportStatusId = 1;				// 1=정상,클린한 유저
			} else if ( calTrustScore >= 40 ) {
				reportStatusId = 2;				// 2=주의,선 넘은 어그로 유저
			} else {
				reportStatusId = 3;				// 3=정지,진실의 방으로...
			}
			
			ReportsDto updateDto = new ReportsDto();
			updateDto.setMemberId(targetMemberId);			// 신고대상id
			updateDto.setTrustScore(calTrustScore);			// 신뢰도점수
			updateDto.setReportStatusId(reportStatusId);	// 상태 번호 (status_name 출력)
			
			dao.updateMemberTrustScore(updateDto);		// 신뢰도 점수 update
			dao.updateMemberReportStatusId(updateDto);	// 뱃지 상태 update
		}
		
		
		///////////////////////////////////////////////////
		// apiEmail content
		String subject = "신고 처리되지 않음.";
		String content = "신고 처리되지 않음.";
		if( "APPROVED".equals(dto.getStatus()) ) {
			subject = "[APPROVED] 신고 처리가 승인 되었습니다.";
			content = "[APPROVED] 신고 처리가 승인 되었습니다.";
			
		} else if( "REJECTED".equals(dto.getStatus()) ) {
			subject = "[REJECTED] 신고 처리가 반려 되었습니다.";
			content = "[REJECTED] 신고 처리가 반려 되었습니다.";
		}
		
		// apiEmail Email
		String email = dao.selectEmail(dto);
		
		apiEmail.sendMail(subject, content, email); //메일 test
		return result;
	}
	
	@Override
	public int deleteAdmin(int reportId) {
		
		ReportsDto dto = new ReportsDto();
		dto.setReportId(reportId);

		// apiEmail Email
		String email = dao.selectEmail(dto);
		
		// apiEmail content
		int result = dao.deleteAdmin(reportId);

		if( result > 0 ) {
			String subject = "Moit 신고 문의 처리";
			String content = "신고 글이 삭제 되었습니다.";
			
			if( email != null ) {
				apiEmail.sendMail(subject, content, email); //메일 test
			} else { System.out.println("메일 전송 실패..."); }
		}
		
		return result;
	}
	
	
	@Override // 관리자 신고 목록 조회 (동적 조건 + 페이징 + 단건 조회까지 포함)
	public List<ReportsDto> selectAdminReports(HashMap<String, Object> map) {
		
		List<ReportsDto> list = dao.selectAdminReports(map);

		for (ReportsDto dto : list) { 
			Integer targetMemberId = dao.selectTargetMemberId(dto); // 신고당한 글 작성자 조회
			
			//adminDetail.html
			if (targetMemberId == null) {
	            dto.setTargetNickname("대상 없음");
	            dto.setTrustScore(0);
	            dto.setStatusName("조회불가");
	            continue;
	        }
			
			// 신고당한 유저
			dto.setTargetMemberId(targetMemberId);
			
			ReportsDto searchParam = new ReportsDto();
			searchParam.setTargetMemberId(targetMemberId);

			ReportsDto trustInfo = dao.findMemberTrustInfo(searchParam);
			
			if (trustInfo != null) {
				dto.setTargetMemberId( trustInfo.getTargetMemberId() );
				dto.setTargetNickname( trustInfo.getTargetNickname() );
				dto.setTrustScore( trustInfo.getTrustScore() );
				
				// 뱃지
				dto.setReportStatusId( trustInfo.getReportStatusId() );
				dto.setStatusCode( trustInfo.getStatusCode() );
				dto.setStatusName( trustInfo.getStatusName() );
			}
		}
		
		return list;
	}

	@Override // 관리자 신고 목록 카운트 (동적 조건 반영)
	public int selectAdminReportsCnt(HashMap<String, Object> map) {
		return dao.selectAdminReportsCnt(map);
	}
	
	

	@Override // 3일 전에 신고 상태 변경된 데이터 추출
	public List<ReportsDto> selectThreeDaysAgo() {
		
		List<ReportsDto> targetList = dao.selectThreeDaysAgo();
//		System.out.println( targetList.size() );
		
		for (ReportsDto target : targetList) {
			String email = target.getEmail();
			
			if( email != null && !email.isEmpty() ) {
				String subject = "[만족도 참여] Moit 문의 처리 결과는 어떠셨나요?";
				String content = "Moit 문의 처리 결과는 어떠셨나요?<br>"
								+ "마음에 드셨다면 만족도 참여에 동참해주세요!";
				
				try {
					apiEmail.sendMail(subject, content, email); //메일 test
				} catch (Exception e) { e.printStackTrace(); }
				
			} else { System.out.println("메일 전송 실패..."); }
		}
		return targetList;
	}

	// 
	@Override
	public List<ReportsDto> selectTargetMembersYesterday() {
		return dao.selectTargetMembersYesterday();
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
