package com.moit.reports.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.moit.reports.dto.MemberTrustInfoDto;
import com.moit.reports.dto.ReportAuditLogDto;
import com.moit.reports.dto.ReportSearchDto;
import com.moit.reports.dto.ReportsDto.ReportListResponseDto;
import com.moit.reports.dto.ReportsDto.ReportProcessDto;
import com.moit.reports.dto.ReportsDto.ReportRequestDto;
import com.moit.reports.dto.ReportsDto.ReportResponseDto;
import com.moit.reports.enums.TargetType;

public interface ReportsService {
	
	// ============
	// =   user   =
	// ============
	// 사용자 신고 작성
	ReportResponseDto createUserReport(Long memberId, ReportRequestDto requestDto);
	
	// 사용자 신고 수정
	// findByReportIdAndMember_IdAndDeleteYnAndStatus
	ReportResponseDto updateUserReport(Long reportId, Long memberId, ReportRequestDto requestDto);
	
	// 사용자 신고 삭제 (논리삭제)
	void deleteUserReport(Long reportId, Long memberId);
	
	// 사용자 신고 목록 조회 + 페이징
	// findByMember_IdAndDeleteYnOrderByReportIdDesc
	ReportListResponseDto getUserReports(Long memberId, Pageable pageable);
	
	// 사용자 신고 상세 조회
	// findByReportIdAndMember_IdAndDeleteYn
	ReportResponseDto getUserReportDetail(Long reportId, Long memberId);

	// 중복 신고 확인
	// existsByMember_IdAndTargetTypeAndTargetIdAndDeleteYn
	boolean checkDoubleReport(Long memberId, TargetType targetType, Long targetId);

	
	// =============
	// =   admin   =
	// =============
	// 관리자 처리 상태 (승인/반려/신뢰도점수) 변경
	// findByReportIdAndStatus
	void updateAdminReport(Long reportId, Long adminMemberId, ReportProcessDto processDto);
	
	// 관리자 신고 삭제 (물리삭제)
	void deleteAdminReport(Long reportId);
	
	// 관리자 신고 목록 조회 + 검색 + 페이징
	ReportListResponseDto getAdminReports(ReportSearchDto searchDto, Pageable pageable);

	// 관리자 신고 상세 조회
	ReportResponseDto getAdminReportDetail(Long reportId);
	
	
	// =====================
	// =   adminAuditLog   =
	// =====================
	// 신고별 관리자 처리 이력 로그 조회
	List<ReportAuditLogDto> getReportAuditLogs(Long reportId);
	
	
	// ==================
	// =   trustScore   =
	// ==================
	// 신고당한 회원 (신뢰도점수/뱃지) 조회
	MemberTrustInfoDto getMemberTrustInfo(Long targetMemberId);

	
	// ================
	// =   apiEmail   =
	// ================
	// 3일 전 신고 처리한 이메일 조회
	List<String> getThreeDaysAgoReportEmails();
}


//// [ㅇ] 사용자 본인이 작성한 신고 내역 조회 & 유저 - 페이징
//public List<ReportsDto> selectUserReport( int pstartno, int memberId);
//
//// [] select id="selectUserCnt" resultType="int"
//public int selectUserCnt(Long memberId);
//
//// [ㅇ] 사용자 본인이 작성한 신고 내역 상세 조회
//public ReportsDto selectUserReportDetail(ReportsDto dto);
//
//// [ㅇ] 신고 작성 기능
//public int insertUserReport(ReportsDto dto);
//
//// [ㅇ] 신고 수정 기능 update
//public int updateUserReport(ReportsDto dto);
//
//// [ㅇ] 신고 내역 삭제 (update delete_yn = 'Y')
//public int deleteUserReport(ReportsDto dto);
//
//// [ㅇ] 모임,리뷰 신고 더블 체크
//public int checkDoubleReport(ReportsDto dto);
//
//// ===== admin =====
//// ===== admin =====
//public int updateAdmin(ReportsDto dto);
//public int deleteAdmin(int reportId);
//
//// [ㅇ] 관리자 신고 목록 조회 (동적 조건 + 페이징 + 단건 조회까지 포함)
//public List<ReportsDto> selectAdminReports(HashMap<String, Object> map);
//
//// [] 관리자 신고 목록 카운트 (동적 조건 반영)
//public int selectAdminReportsCnt(HashMap<String, Object> map);
//
////////////////////
//// email send	//
////////////////////
//
//// [] 3일 전에 신고 상태 변경된 데이터 추출
//public List<ReportsDto> selectThreeDaysAgo();
//
//// 새벽배치용 approved, noshow, reportCnt 계산
//public List<ReportsDto> selectTargetMembersYesterday();