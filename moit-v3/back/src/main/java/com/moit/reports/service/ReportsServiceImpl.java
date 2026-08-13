package com.moit.reports.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moit.reports.api.ApiEmail;
import com.moit.reports.dto.MemberTrustInfoDto;
import com.moit.reports.dto.ReportAuditLogDto;
import com.moit.reports.dto.ReportSearchDto;
import com.moit.reports.dto.ReportsDto.ReportListResponseDto;
import com.moit.reports.dto.ReportsDto.ReportProcessDto;
import com.moit.reports.dto.ReportsDto.ReportRequestDto;
import com.moit.reports.dto.ReportsDto.ReportResponseDto;
import com.moit.reports.enums.TargetType;
import com.moit.reports.repository.MemberReportStatusRepository;
import com.moit.reports.repository.ReportAuditLogRepository;
import com.moit.reports.repository.ReportRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportsServiceImpl implements ReportsService {
	
	private final ReportRepository reportRepository;
	private final MemberReportStatusRepository memberReportStatusRepository;
	private final ReportAuditLogRepository reportAuditLogRepository;
	private final ApiEmail apiEmail;
	
	@Override
	public ReportResponseDto createUserReport(Long memberId, ReportRequestDto requestDto) {



		return null;
	}

	@Override
	public ReportResponseDto updateUserReport(Long reportId, Long memberId, ReportRequestDto requestDto) {
//		@Override // 신고 수정 화면 update
//		public int updateUserReport(ReportsDto dto) {
//			return dao.updateUserReport(dto);
//		}
	//

		return null;
	}

	@Override
	public void deleteUserReport(Long reportId, Long memberId) {
//		@Override // 신고 내역 삭제 (update delete_yn = 'Y')
//		public int deleteUserReport(ReportsDto dto) {
//			return dao.deleteUserReport(dto);
//		}
		
	}

	@Override
	public ReportListResponseDto getUserReports(Long memberId, Pageable pageable) {
//		@Override // 사용자 본인이 작성한 신고 내역 조회 & 유저 - 페이징
//		public List<ReportsDto> selectUserReport(int pstartno, int memberId) {
	//
//			HashMap<String,Object> map = new HashMap<>();
//			map.put("start", (pstartno-1)*10);  
//			map.put("end"  ,  10);
//			map.put("memberId"  ,  memberId);
//			
//			return dao.selectUserReport(map);
//		}
	//
//		@Override // select id="selectUserCnt" resultType="int"
//		public int selectUserCnt(Long memberId) {
//		    return dao.selectUserCnt(memberId);
//		}
		return null;
	}

	@Override
	public ReportResponseDto getUserReportDetail(Long reportId, Long memberId) {
//		@Override // 사용자 본인이 작성한 신고 내역 상세 조회
//		public ReportsDto selectUserReportDetail(ReportsDto dto) {
//			
//			// targetMemberId 회원 정보 조회 test
//			Integer targetMemberId = dao.selectTargetMemberId(dto);
//			System.out.println( "타겟멤버아이디 찍힘? " + targetMemberId );
	//
//			// detail 신고 목록 상세 조회
//			ReportsDto detail = dao.selectUserReportDetail(dto);
//			detail.setTargetMemberId(targetMemberId);
//			
//			// 닉네임 신뢰도 뱃지
////			ReportsDto trustInfo = dao.findMemberTrustInfo(dto);
//			ReportsDto trustInfo = dao.findMemberTrustInfo(detail);
//			
//			if( trustInfo != null ) {
//				detail.setTargetMemberId( trustInfo.getTargetMemberId() );
//				detail.setTargetNickname( trustInfo.getTargetNickname() );
//				detail.setTrustScore( trustInfo.getTrustScore() );
//				
//				detail.setReportStatusId( trustInfo.getReportStatusId() );
//				detail.setStatusCode( trustInfo.getStatusCode() );
//				detail.setStatusName( trustInfo.getStatusName() );
//			}
//			System.out.println( "닉네임 신뢰도 뱃지 출력 test : " + trustInfo );
//			System.out.println( "HTML로 반환할 detail = " + detail );
	//
//			return detail;
////			return dao.selectUserReportDetail(dto);
//		}
		return null;
	}

	@Override
	public boolean checkDoubleReport(Long memberId, TargetType targetType, Long targetId) {
//		//모임,리뷰 신고 더블 체크
//		@Override
//		public int checkDoubleReport(ReportsDto dto) {
	//
//		    int count = dao.doubleReport(dto);
	//
//		    if(count > 0) {
//		        return -1;
//		    }
//		    return 0;
//		}
		return false;
	}

	@Override
	public void updateAdminReport(Long reportId, Long adminMemberId, ReportProcessDto processDto) {
//		@Override
//		public int updateAdmin(ReportsDto dto) {
//			
//			// rejected or approved
//			int result = dao.updateAdmin(dto);
//			
//			if( "APPROVED".equals(dto.getStatus()) ) {	// status가 APPROVED라면
//				int targetMemberId = dao.selectTargetMemberId(dto); // 신고당한 대상 아이디(정보) 불러오기
//				dto.setTargetMemberId(targetMemberId);	
//				
//				// 신뢰도 점수 sql 쿼리 3개
//				//			int approvedCnt = dao.selectApprovedCnt(targetMemberId);
//				//			int noshowCnt = dao.selectNoshowCnt(targetMemberId);
//				//			int reportCnt = dao.selectReportCnt(targetMemberId);
//				// 계산
//				//		int trustScore = 100 + (approvedCnt * 2) - (noshowCnt * 10) - (reportCnt * 5);
	//
//				// 신뢰도 점수 sql 쿼리 1개
//				int calTrustScore = dao.calTrustScore(targetMemberId);
	//
//				int reportStatusId = 1;
//				if( calTrustScore >= 80 ) {
//					reportStatusId = 1;				// 1=정상,클린한 유저
//				} else if ( calTrustScore >= 40 ) {
//					reportStatusId = 2;				// 2=주의,선 넘은 어그로 유저
//				} else {
//					reportStatusId = 3;				// 3=정지,진실의 방으로...
//				}
//				
//				ReportsDto updateDto = new ReportsDto();
//				updateDto.setMemberId(targetMemberId);			// 신고대상id
//				updateDto.setTrustScore(calTrustScore);			// 신뢰도점수
//				updateDto.setReportStatusId(reportStatusId);	// 상태 번호 (status_name 출력)
//				
//				dao.updateMemberTrustScore(updateDto);			// 신뢰도 점수 update
//				dao.updateMemberBadge(updateDto);				// 뱃지 상태 update
//			}
		
	}

	@Override
	public void deleteAdminReport(Long reportId) {
//		@Override
//		public int deleteAdmin(int reportId) {
//			
//			ReportsDto dto = new ReportsDto();
//			dto.setReportId(reportId);
	//
//			// apiEmail Email
//			String email = dao.selectEmail(dto);
//			
//			// apiEmail content
//			int result = dao.deleteAdmin(reportId);
	//
//			if( result > 0 ) {
//				String subject = "Moit 신고 문의 처리";
//				String content = "신고 글이 삭제 되었습니다.";
//				
//				if( email != null ) {
//					apiEmail.sendMail(subject, content, email); //메일 test
//				} else { System.out.println("메일 전송 실패..."); }
//			}
//			
//			return result;
//		}
		
	}

	@Override
	public ReportListResponseDto getAdminReports(ReportSearchDto searchDto, Pageable pageable) {
//		@Override // 관리자 신고 목록 조회 (동적 조건 + 페이징 + 단건 조회까지 포함)
//		public List<ReportsDto> selectAdminReports(HashMap<String, Object> map) {
//			
//			List<ReportsDto> list = dao.selectAdminReports(map);
	//
//			for (ReportsDto dto : list) { 
//				Integer targetMemberId = dao.selectTargetMemberId(dto); // 신고당한 글 작성자 조회
//				
//				//adminDetail.html
//				if (targetMemberId == null) {
//					dto.setTargetNickname("대상 없음");
//					dto.setTrustScore(0);
//					dto.setStatusName("조회불가");
//					continue;
//				}
//				// 신고당한 유저
//				dto.setTargetMemberId(targetMemberId);
//				System.out.println("신고당한 글 작성자 조회 출력: " + targetMemberId);
	//
//				
//				// 닉네임, 신뢰도 점수, 뱃지
//				ReportsDto searchParam = new ReportsDto();
//				searchParam.setTargetMemberId(targetMemberId);
	//
//				ReportsDto trustInfo = dao.findMemberTrustInfo(searchParam);
//				
//				
//				if (trustInfo != null) {
//					dto.setTargetMemberId( trustInfo.getTargetMemberId() );
//					dto.setTargetNickname( trustInfo.getTargetNickname() );
//					dto.setTrustScore( trustInfo.getTrustScore() );
//					
//					dto.setReportStatusId( trustInfo.getReportStatusId() );
//					dto.setStatusCode( trustInfo.getStatusCode() );
//					dto.setStatusName( trustInfo.getStatusName() );
//				}
//				System.out.println( "신뢰도 점수, 뱃지 test : " + trustInfo);
//			}
//			
//			return list;
//		}
	//
//		@Override // 관리자 신고 목록 카운트 (동적 조건 반영)
//		public int selectAdminReportsCnt(HashMap<String, Object> map) {
//			return dao.selectAdminReportsCnt(map);
//		}
		return null;
	}

	@Override
	public ReportResponseDto getAdminReportDetail(Long reportId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReportAuditLogDto> getReportAuditLogs(Long reportId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberTrustInfoDto getMemberTrustInfo(Long targetMemberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getThreeDaysAgoReportEmails() {
//		@Override // 3일 전에 신고 상태 변경된 데이터 추출
//		public List<ReportsDto> selectThreeDaysAgo() {
//			
//			List<ReportsDto> targetList = dao.selectThreeDaysAgo();
////			System.out.println( targetList.size() );
//			
//			for (ReportsDto target : targetList) {
//				String email = target.getEmail();
//				
//				if( email != null && !email.isEmpty() ) {
//					String subject = "[만족도 참여] Moit 문의 처리 결과는 어떠셨나요?";
//					String content = "Moit 문의 처리 결과는 어떠셨나요?<br>"
//									+ "마음에 드셨다면 만족도 참여에 동참해주세요!";
//					
//					try {
//						apiEmail.sendMail(subject, content, email); //메일 test
//					} catch (Exception e) { e.printStackTrace(); }
//					
//				} else { System.out.println("메일 전송 실패..."); }
//			}
//			return targetList;
//		}
		return null;
	}
}


	
//		///////////////////////////////////////////////////
//		// apiEmail content
//		String subject = "신고 처리되지 않음.";
//		String content = "신고 처리되지 않음.";
//		if( "APPROVED".equals(dto.getStatus()) ) {
//			subject = "[APPROVED] 신고 처리가 승인 되었습니다.";
//			content = "[APPROVED] 신고 처리가 승인 되었습니다.";
//			
//		} else if( "REJECTED".equals(dto.getStatus()) ) {
//			subject = "[REJECTED] 신고 처리가 반려 되었습니다.";
//			content = "[REJECTED] 신고 처리가 반려 되었습니다.";
//		}
//		
//		// apiEmail Email
//		String email = dao.selectEmail(dto);
//		
//		apiEmail.sendMail(subject, content, email); //메일 test
//		return result;
//	}