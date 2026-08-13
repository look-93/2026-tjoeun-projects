package com.moit.reports.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.member.entity.Member;
import com.moit.member.entity.MemberInfo;
import com.moit.member.repository.MemberInfoRepository;
import com.moit.member.repository.MemberRepository;
import com.moit.reports.api.ApiEmail;
import com.moit.reports.dto.MemberTrustInfoDto;
import com.moit.reports.dto.ReportAuditLogDto;
import com.moit.reports.dto.ReportSearchDto;
import com.moit.reports.dto.ReportsDto;
import com.moit.reports.dto.ReportsDto.ReportListResponseDto;
import com.moit.reports.dto.ReportsDto.ReportProcessDto;
import com.moit.reports.dto.ReportsDto.ReportRequestDto;
import com.moit.reports.dto.ReportsDto.ReportResponseDto;
import com.moit.reports.entity.Report;
import com.moit.reports.enums.ReportStatus;
import com.moit.reports.enums.TargetType;
import com.moit.reports.repository.MemberReportStatusRepository;
import com.moit.reports.repository.ReportAuditLogRepository;
import com.moit.reports.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportsServiceImpl implements ReportsService {
	
	private final ReportRepository reportRepository;
	private final MemberReportStatusRepository memberReportStatusRepository;
	private final ReportAuditLogRepository reportAuditLogRepository;
	private final ApiEmail apiEmail;
	
	private final MemberRepository memberRepository;
	private final MemberInfoRepository memberInfoRepository;
	
	// 사용자 신고 작성
	@Override
	@Transactional
	public ReportResponseDto createUserReport(Long memberId, ReportRequestDto requestDto) {
		Member user = memberRepository.findById(memberId)
				.orElseThrow(()-> new IllegalArgumentException("사용자 신고 작성 오류! 존재하지 않는 사용자! ID: " + memberId));
		
		// 중복 신고 확인 코드 추가
		boolean doubleCheck = reportRepository
				.existsByMember_IdAndTargetTypeAndTargetIdAndDeleteYn(
				        memberId, requestDto.getTargetType(), requestDto.getTargetId(), 'N');
		
		// 신고 글 작성
		Report report = new Report();
		report.setTargetType( requestDto.getTargetType() );
		report.setTargetId( requestDto.getTargetId() );
		report.setMember(user);
		report.setReasonCode( requestDto.getReasonCode());
		report.setReasonDetail( requestDto.getReasonDetail() );
		report.setStatus(ReportStatus.PENDING);
		Report savedReport = reportRepository.save(report);
		
		return ReportResponseDto.from(savedReport);
	}

	// 사용자 신고 수정
	@Override
	@Transactional
	public ReportResponseDto updateUserReport(Long reportId, Long memberId, ReportRequestDto requestDto) {
		Report report = reportRepository
				.findByReportIdAndMember_IdAndDeleteYnAndStatus(reportId, memberId, 'N', ReportStatus.PENDING)
				.orElseThrow(()-> new IllegalArgumentException("사용자 신고 수정 오류! reportId: " + reportId));

		report.setReasonCode( requestDto.getReasonCode());	// 저장메서드를 따로 호출하지 않아도 update 쿼리 반영
		report.setReasonDetail( requestDto.getReasonDetail() );
		
		return ReportResponseDto.from(report);
	}

	// 사용자 신고 삭제 (논리삭제 update delete_yn = 'Y')
	@Override
	@Transactional
	public void deleteUserReport(Long reportId, Long memberId) {
		// 삭제할 신고 조회
	    Report report = reportRepository
	    		.findByReportIdAndMember_IdAndDeleteYnAndStatus(reportId, memberId, 'N', ReportStatus.PENDING)
	            .orElseThrow(()-> new IllegalArgumentException("사용자 신고 삭제 오류! reportId: " + reportId));

	    // 신고 물리삭제
//	    reportRepository.delete(report);
	    // 신고 논리삭제
	    report.setDeleteYn('Y');
	}

	// 사용자 신고 목록 조회 + 페이징
	@Override
	public ReportListResponseDto getUserReports(Long memberId, Pageable pageable) {
		Page<Report> page = reportRepository
				.findByMember_IdAndDeleteYnOrderByReportIdDesc(memberId, 'N', pageable);
		
		// 조회된 신고 Entity 목록을 ResponseDto 목록으로 변환
		List<ReportResponseDto> response = page.getContent()
				.stream()
				.map(ReportResponseDto::from)
				.toList();
		
		ReportListResponseDto responseDto = new ReportListResponseDto();
		// 신고 목록
		responseDto.setReports(response);
		// 전체 신고 개수					- long
		responseDto.setTotalCount(page.getTotalElements());
		// 전체 페이지 수 (20/10 = 2...)	- int
		responseDto.setTotalPage((long) page.getTotalPages());
		
		return responseDto;
	}

	// 사용자 신고 상세 조회
	@Override
	public ReportResponseDto getUserReportDetail(Long reportId, Long memberId) {
		Report report = reportRepository
				.findByReportIdAndMember_IdAndDeleteYn(reportId, memberId, 'N')
				.orElseThrow(()-> new IllegalArgumentException("사용자 신고 수정 오류! 존재하지 않는 신고! reportId: " + reportId));
		
		return ReportResponseDto.from(report);
	}

	// 모임, 리뷰 신고 더블 체크
	@Override
	public boolean checkDoubleReport(Long memberId, TargetType targetType, Long targetId) {
//		int count = dao.doubleReport(dto);
//		if(count > 0) { return -1; }
//		return 0;
		// -1은 중복ㅇㅇ,		0은 중복ㄴㄴ

		// true = 중복ㅇㅇ,	false = 중복ㄴㄴ
		return reportRepository.existsByMember_IdAndTargetTypeAndTargetIdAndDeleteYn(memberId, targetType, targetId, 'N');
	}

	// 관리자 처리 상태 (승인/반려/신뢰도점수/감사로그) 변경
	@Override
	@Transactional
	public void updateAdminReport(Long reportId, Long MemberId, ReportProcessDto processDto) {
		Report report = reportRepository.findById(reportId, ReportStatus.PENDING)
				.orElseThrow(()-> new IllegalArgumentException("관리자 신고 수정 오류! 처리 가능한 신고 없음! reportId: " + reportId));
		
		// 상태 변경 - 변경 후
		ReportStatus changedStatus = processDto.getStatus();
		// APPROVED / REJECTED만 허용
	    if (changedStatus != ReportStatus.APPROVED && changedStatus != ReportStatus.REJECTED) {
	        throw new IllegalArgumentException("신고 상태는 APPROVED 또는 REJECTED만 가능합니다.");
	    }
		
		// 상태 변경 - 변경 전
	    ReportStatus previousStatus = report.getStatus();
		
		// 신뢰도 변경
		int trustScoreChange = 0;
		
		if(changedStatus == ReportStatus.APPROVED) {	// status가 APPROVED라면
			int targetMemberId = dao.selectTargetMemberId(dto); // 신고당한 대상 아이디(정보) 불러오기
			dto.setTargetMemberId(targetMemberId);	
			
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
			
			dao.updateMemberTrustScore(updateDto);			// 신뢰도 점수 update
			dao.updateMemberBadge(updateDto);				// 뱃지 상태 update
		}
		
		// 관리자 처리 감사 로그 저장
			
		// 이메일 조회 및 전송
		String email = report.getMember().getEmail();
		String subject = "신고 처리되지 않음.";
		String content = "신고 처리되지 않음.";
		
		if(changedStatus == Status.APPROVED) {
		subject = "[APPROVED] 신고 처리가 승인 되었습니다.";
		content = "[APPROVED] 신고 처리가 승인 되었습니다.";
		
		} else if(changedStatus == Status.REJECTED) {
		subject = "[REJECTED] 신고 처리가 반려 되었습니다.";
		content = "[REJECTED] 신고 처리가 반려 되었습니다.";
		}
		
		// 메일 전송 test
	    if (email != null && !email.isBlank()) { apiEmail.sendMail(subject, content, email); }
	    else { System.out.println("이메일이 없습니다. 메일 전송 실패..."); }
	}

	// 관리자 신고 삭제 (물리삭제)
	@Override
	@Transactional
	public void deleteAdminReport(Long reportId) {
		// 삭제할 신고 조회
	    Report report = reportRepository.findById(reportId)
	            .orElseThrow(()-> new IllegalArgumentException("신고를 찾을 수 없습니다.") );

	    // 신고 작성자 이메일 조회 (신고 작성자 회원 = Report.member)
	    String email = null;
	    
	    if (report.getMember() != null) {
	        email = report.getMember().getEmail();
	    }

	    // 신고 물리삭제
	    reportRepository.delete(report);

	    // 삭제 완료 메일 발송
	    String subject = "[DELETE] Moit 신고 문의 처리";
	    String content = "신고 글이 삭제 되었습니다.";

	    // 메일 전송 test
	    if (email != null && !email.isBlank()) { apiEmail.sendMail(subject, content, email); }
	    else { System.out.println("이메일이 없습니다. 메일 전송 실패..."); }
	}

	// 관리자 신고 목록 조회 + 검색 + 페이징
	@Override
	public ReportListResponseDto getAdminReports(ReportSearchDto searchDto, Pageable pageable) {
		Page<Report> page = reportRepository
				.findByMember_IdAndDeleteYnOrderByReportIdDesc(memberId, 'N', pageable);
		
		// 조회된 신고 Entity 목록을 ResponseDto 목록으로 변환
		List<ReportResponseDto> response = page.getContent()
				.stream()
				.map(ReportResponseDto::from)
				.toList();
		
		ReportListResponseDto responseDto = new ReportListResponseDto();
		// 신고 목록
		responseDto.setReports(response);
		// 전체 신고 개수					- long
		responseDto.setTotalCount(page.getTotalElements());
		// 전체 페이지 수 (20/10 = 2...)	- int
		responseDto.setTotalPage((long) page.getTotalPages());
		
		return responseDto;
	}
		
	// 관리자 신고 상세 조회
	@Override
	public ReportResponseDto getAdminReportDetail(Long reportId) {
		Report report = reportRepository.findById(reportId)
				.orElseThrow(()-> new IllegalArgumentException("관리자 신고 상세 조회 오류! reportId: " + reportId));
		
		return ReportResponseDto.from(report);
	}

	// 관리자 처리 로그 조회
	@Override
	public List<ReportAuditLogDto> getReportAuditLogs(Long reportId) {
		Report report = reportAuditLogRepository
				.findByReport_ReportIdOrderByProcessedAtDesc(reportId)
				.orElseThrow(()-> new IllegalArgumentException("관리자 처리 로그 조회! reportId: " + reportId));
		
		return ReportResponseDto.from(report);
	}

	// 신고당한 회원 (신뢰도점수/뱃지) 조회
	@Override
	public MemberTrustInfoDto getMemberTrustInfo(Long targetMemberId) {
		MemberInfo memberInfo = memberInfoRepository
				.findById(targetMemberId)
				.orElseThrow(()-> new IllegalArgumentException("신고당한 회원 (신뢰도점수/뱃지) 조회 targetMemberId: " + targetMemberId));
		
		MemberTrustInfoDto memberInfoDto = new MemberTrustInfoDto();
		memberInfoDto.getTargetNickname();	// 신고당한 회원 닉네임
		memberInfoDto.getTrustScore();		// 신뢰도 점수
		memberInfoDto.getStatusName();		// '정상'		/ '주의'		/ '정지'
		
		return ReportResponseDto.from(report);
	}

	
	@Override
	public void sendThreeDaysAgoReportEmails() {
	    List<Report> reports = ;

	    for (Report report : reports) {
	        String email = report.getMember().getEmail();	// 메일주소 조회
	        
	        if( email != null && !email.isEmpty() ) {
				String subject = "[만족도 참여] Moit 문의 처리 결과는 어떠셨나요?";
				String content = "Moit 문의 처리 결과는 어떠셨나요?"
								+ "마음에 드셨다면 만족도 참여에 동참해주세요!"
								+ "링크첨부...";

				//메일 전송 test
				try { apiEmail.sendMail(subject, content, email); }
				catch (Exception e) { e.printStackTrace(); }

	        } else { System.out.println("이메일이 없습니다. 메일 전송 실패..."); }
	    }
	}
}

