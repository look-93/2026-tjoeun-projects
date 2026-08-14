//package com.moit.reports.service;
//
//import java.util.List;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.moit.meetup.entity.Meetup;
//import com.moit.meetup.repository.MeetupRepository;
//import com.moit.member.entity.Member;
//import com.moit.member.entity.MemberInfo;
//import com.moit.member.repository.MemberInfoRepository;
//import com.moit.member.repository.MemberRepository;
//import com.moit.reports.dto.MemberTrustInfoDto;
//import com.moit.reports.dto.ReportAuditLogDto;
//import com.moit.reports.dto.ReportSearchDto;
//import com.moit.reports.dto.ReportsDto.ReportListResponseDto;
//import com.moit.reports.dto.ReportsDto.ReportProcessDto;
//import com.moit.reports.dto.ReportsDto.ReportRequestDto;
//import com.moit.reports.dto.ReportsDto.ReportResponseDto;
//import com.moit.reports.entity.MemberReportStatus;
//import com.moit.reports.entity.Report;
//import com.moit.reports.entity.ReportAuditLog;
//import com.moit.reports.enums.ReportStatus;
//import com.moit.reports.enums.TargetType;
//import com.moit.reports.repository.MemberReportStatusRepository;
//import com.moit.reports.repository.ReportAuditLogRepository;
//import com.moit.reports.repository.ReportRepository;
//import com.moit.review.entity.Review;
//import com.moit.review.repository.ReviewRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class ReportsServiceImpl implements ReportsService {
//	
//	private final ReportRepository reportRepository;
//	private final MemberReportStatusRepository memberReportStatusRepository;
//	private final ReportAuditLogRepository reportAuditLogRepository;
////	private final ApiEmail apiEmail;
//	
//	private final MemberRepository memberRepository;
//	private final MemberInfoRepository memberInfoRepository;
//	
//	private final MeetupRepository meetupRepository;
//	private final ReviewRepository reviewRepository;
//	
//	
//	// 사용자 신고 작성
//	@Override
//	@Transactional
//	public ReportResponseDto createUserReport(Long memberId, ReportRequestDto requestDto) {
//		Member user = memberRepository.findById(memberId)
//				.orElseThrow(()-> new IllegalArgumentException("사용자 신고 작성 오류! 존재하지 않는 사용자! ID: " + memberId));
//		
//		// 중복 신고 확인 코드 추가
//		boolean doubleCheck = reportRepository
//				.existsByMember_IdAndTargetTypeAndTargetIdAndDeleteYn(
//				        memberId, requestDto.getTargetType(), requestDto.getTargetId(), 'N');
//		
//		if (doubleCheck) {
//		    throw new IllegalArgumentException("이미 신고한 대상입니다.");
//		}
//		
//		// 신고 글 작성
//		Report report = new Report();
//		report.setTargetType( requestDto.getTargetType() );
//		report.setTargetId( requestDto.getTargetId() );
//		report.setMember(user);
//		report.setReasonCode( requestDto.getReasonCode());
//		report.setReasonDetail( requestDto.getReasonDetail() );
//		report.setStatus(ReportStatus.PENDING);
//		Report savedReport = reportRepository.save(report);
//		
//		return ReportResponseDto.from(savedReport);
//	}
//
//	// 사용자 신고 수정
//	@Override
//	@Transactional
//	public ReportResponseDto updateUserReport(Long reportId, Long memberId, ReportRequestDto requestDto) {
//		Report report = reportRepository
//				.findByReportIdAndMember_IdAndDeleteYnAndStatus(reportId, memberId, 'N', ReportStatus.PENDING)
//				.orElseThrow(()-> new IllegalArgumentException("사용자 신고 수정 조회 오류! reportId: " + reportId));
//
//		report.setReasonCode( requestDto.getReasonCode());
//		report.setReasonDetail( requestDto.getReasonDetail() );
//		
//		return ReportResponseDto.from(report);
//	}
//
//	// 사용자 신고 삭제 (논리삭제 update delete_yn = 'Y')
//	@Override
//	@Transactional
//	public void deleteUserReport(Long reportId, Long memberId) {
//	    Report report = reportRepository
//	    		.findByReportIdAndMember_IdAndDeleteYnAndStatus(reportId, memberId, 'N', ReportStatus.PENDING)
//	            .orElseThrow(()-> new IllegalArgumentException("사용자 신고 삭제 조회 오류! reportId: " + reportId));
//
//	    // 신고 물리삭제
////	    reportRepository.delete(report);
//	    // 신고 논리삭제
//	    report.setDeleteYn('Y');
//	}
//
//	// 사용자 신고 목록 조회 + 페이징
//	@Override
//	public ReportListResponseDto getUserReports(Long memberId, Pageable pageable) {
//		Page<Report> page = reportRepository
//				.findByMember_IdAndDeleteYnOrderByReportIdDesc(memberId, 'N', pageable);
//		
//		// 조회된 신고 Entity 목록을 ResponseDto 목록으로 변환
//		List<ReportResponseDto> response = page.getContent()
//				.stream()
//				.map(ReportResponseDto::from)
//				.toList();
//		
//		ReportListResponseDto responseDto = new ReportListResponseDto();
//		// 신고 목록
//		responseDto.setReports(response);
//		// 전체 신고 개수					- long
//		responseDto.setTotalCount(page.getTotalElements());
//		// 전체 페이지 수 (20/10 = 2...)	- int
//		responseDto.setTotalPage((long) page.getTotalPages());
//		
//		return responseDto;
//	}
//
//	// 사용자 신고 상세 조회
//	@Override
//	public ReportResponseDto getUserReportDetail(Long reportId, Long memberId) {
//		Report report = reportRepository
//				.findByReportIdAndMember_IdAndDeleteYn(reportId, memberId, 'N')
//				.orElseThrow(()-> new IllegalArgumentException("사용자 신고 상세 조회 오류! reportId: " + reportId));
//		
//		return ReportResponseDto.from(report);
//	}
//
//	// 모임, 리뷰 신고 더블 체크 (화면용 중복 체크)
//	@Override
//	public boolean checkDoubleReport(Long memberId, TargetType targetType, Long targetId) {
//		// -1은 중복ㅇㅇ,		0은 중복ㄴㄴ
////		int count = dao.doubleReport(dto);
////		if(count > 0) { return -1; }
////		return 0;
//
//		// true = 중복ㅇㅇ,	false = 중복ㄴㄴ
//		return reportRepository.existsByMember_IdAndTargetTypeAndTargetIdAndDeleteYn(memberId, targetType, targetId, 'N');
//	}
//
//	// 관리자 처리 상태 (승인/반려/신뢰도점수/감사로그) 변경
//	@Override
//	@Transactional
//	public void updateAdminReport(Long reportId, Long memberId, ReportProcessDto processDto) {
//		Report report = reportRepository
//				.findByReportIdAndStatus(reportId, ReportStatus.PENDING)
//				.orElseThrow(()-> new IllegalArgumentException("관리자 신고 처리 조회 오류! reportId: " + reportId));
//		
//		// 상태 변경 - 변경 전
//		ReportStatus previousStatus = report.getStatus();
//		// 상태 변경 - 변경 후
//		ReportStatus changedStatus = processDto.getStatus();
//		// APPROVED / REJECTED 검증
//	    if (changedStatus != ReportStatus.APPROVED && changedStatus != ReportStatus.REJECTED) {
//	        throw new IllegalArgumentException("신고 상태는 APPROVED 또는 REJECTED만 가능합니다.");
//	    }
//	    // changeStatus 처리 상태 반영
//	    report.changeStatus(changedStatus);
//	    
//	    
//		// 신뢰도점수
//		int trustScoreChange = 0;
//		
//		if (changedStatus == ReportStatus.APPROVED) {	// status가 APPROVED라면
//			
//			trustScoreChange = -5;
//			Long targetMemberId = null;
//			
//			if (report.getTargetType() == TargetType.MEETUP) {
//				// report.getTargetId()로 Meetup 조회
//				Long meetupId = report.getTargetId();
//				// Meetup 작성자의 memberId 가져오기
//				Meetup meetup = meetupRepository
//						.findById(meetupId)
//						.orElseThrow(()-> new IllegalArgumentException("Meetup 작성자의 memberId 불러오기 실패!"));
//						
//				targetMemberId = meetup.getMember().getId();
//				
//				
//			} else if (report.getTargetType() == TargetType.REVIEW) {
//				// report.getTargetId()로 Review 조회
//				Long reviewId = report.getTargetId();
//				// Review 작성자의 memberId 가져오기
//				Review review = reviewRepository
//						.findById(reviewId)
//						.orElseThrow(()-> new IllegalArgumentException("Review 작성자의 memberId 불러오기 실패!"));
//				
//				targetMemberId = review.getMember().getId();
//			
//			} else {
//				throw new IllegalArgumentException("MEETUP, REVIEW 가 아닌 다른 targetType 입니다.");
//			}
//			
//			// memberInfo 조회
//			MemberInfo memberInfo = memberInfoRepository
//					.findById(targetMemberId)
//					.orElseThrow(()-> new IllegalArgumentException("신고 대상 회원 MemberInfo 조회 불가!"));
//			
//			// 신뢰도 점수 반영
//			int currentTrustScore = memberInfo.getTrustScore();
//			int changedTrustScore = currentTrustScore + trustScoreChange;
//			memberInfo.setTrustScore(changedTrustScore);
//			
//			// 뱃지 변경
//			String statusCode;
//			
//			if (changedTrustScore >= 80) {
//				statusCode = "ACTIVE";
//			} else if (changedTrustScore >= 40) {
//				statusCode = "WARNING";
//			} else {
//				statusCode = "DANGER";
//			}
//			
//			MemberReportStatus memberReportStatus = memberReportStatusRepository
//					.findByStatusCode(statusCode)
//					.orElseThrow(()-> new IllegalArgumentException("회원 신고 상태 조회 불가!"));
//			
//			memberInfo.setReportStatus(memberReportStatus);
//		}
//		
//		// 관리자 Member 조회
//	    Member adminMember = memberRepository
//	    		.findById(memberId)
//				.orElseThrow(()-> new IllegalArgumentException("관리자 조회 오류! MemberId: " + memberId));
//	    
//		// 관리자 처리 감사 로그 (상태) 저장
//		ReportAuditLog reportAuditLog = ReportAuditLog.statusChanged (
//				report,
//				adminMember,
//				previousStatus,
//				changedStatus,
//				processDto.getProcessReason(),
//				trustScoreChange
//		);
//		
//		reportAuditLogRepository.save(reportAuditLog);
//			
//		// 이메일 조회 및 전송
//		String email = report.getMember().getEmail();
//		String subject = "신고 처리되지 않음.";
//		String content = "신고 처리되지 않음.";
//		
//		if(changedStatus == ReportStatus.APPROVED) {
//		subject = "[APPROVED] 신고 처리가 승인 되었습니다.";
//		content = "[APPROVED] 신고 처리가 승인 되었습니다.";
//		
//		} else if(changedStatus == ReportStatus.REJECTED) {
//		subject = "[REJECTED] 신고 처리가 반려 되었습니다.";
//		content = "[REJECTED] 신고 처리가 반려 되었습니다.";
//		}
//		
//		// 메일 전송 test
////	    if (email != null && !email.isBlank()) { apiEmail.sendMail(subject, content, email); }
////	    else { System.out.println("이메일이 없습니다. 메일 전송 실패..."); }
//	}
//
//	// 관리자 신고 삭제 (물리삭제 -> 논리삭제 변경 + 감사 로그 processReason 포함)
//	@Override
//	@Transactional
//	public void deleteAdminReport(Long reportId, Long memberId, String processReason) {
//		// 삭제할 신고 조회
//	    Report report = reportRepository
//	    		.findById(reportId)
//	            .orElseThrow(()-> new IllegalArgumentException("삭제할 신고를 조회할 수 없습니다.") );
//	    
//	    if (processReason == null || processReason.isEmpty()) {
//	    	throw new IllegalArgumentException("삭제 사유를 입력해주세요.");
//	    }
//
//	    // 신고 물리삭제
////	    reportRepository.delete(report);
//	    // 신고 논리삭제
//	    report.setDeleteYn('Y');
//	    
//	    // 관리자 Member 조회
//	    Member adminMember = memberRepository
//	    		.findById(memberId)
//				.orElseThrow(()-> new IllegalArgumentException("관리자 조회 오류! MemberId: " + memberId));
//	    
//		// 관리자 처리 감사 로그 (삭제) 저장
//		ReportAuditLog reportAuditLog = ReportAuditLog.deleted(
//				report,
//				adminMember,
//				processReason
//		);
//		reportAuditLogRepository.save(reportAuditLog);
//		
//		// 신고 작성자 이메일 조회 (신고 작성자 회원 = Report.member)
//	    String email = report.getMember().getEmail();
//	    
//	    // 삭제 완료 메일 발송
//	    String subject = "[DELETE] Moit 신고 문의 처리";
//	    String content = "신고 글이 삭제 되었습니다.";
//
//	    // 메일 전송 test
////	    if (email != null && !email.isBlank()) { apiEmail.sendMail(subject, content, email); }
////	    else { System.out.println("이메일이 없습니다. 메일 전송 실패..."); }
//	}
//
//	// 관리자 신고 목록 조회 + 검색 + 페이징
//	@Override
//	public ReportListResponseDto getAdminReports(ReportSearchDto searchDto, Pageable pageable) {
////		@Param(value="status") ReportStatus status,
////		@Param(value="deleteYn") Character deleteYn,
////		@Param(value="targetType") TargetType targetType,
////		@Param(value="memberNickname") String memberNickname,
////		@Param(value="reasonCode") ReasonCode reasonCode
//		Page<Report> page = reportRepository.findAdminReports(
//				searchDto.getStatus(),
//				searchDto.getDeleteYn(),
//				searchDto.getTargetType(),
//				searchDto.getMemberNickname(),
//				searchDto.getReasonCode(),
//				pageable
//		);
//		
//		// 조회된 신고 Entity 목록을 ResponseDto 목록으로 변환
//		List<ReportResponseDto> response = page.getContent()
//				.stream()
//				.map(ReportResponseDto::from)
//				.toList();
//		
//		ReportListResponseDto responseDto = new ReportListResponseDto();
//		responseDto.setReports(response);	// 신고 목록
//		responseDto.setTotalCount(page.getTotalElements());		// 전체 신고 개수
//		responseDto.setTotalPage((long) page.getTotalPages());	// 전체 페이지 수 (20/10 = 2...)
//		
//		return responseDto;
//	}
//		
//	// 관리자 신고 상세 조회
//	@Override
//	public ReportResponseDto getAdminReportDetail(Long reportId) {
//		Report report = reportRepository.findById(reportId)
//				.orElseThrow(()-> new IllegalArgumentException("관리자 신고 상세 조회 오류! reportId: " + reportId));
//		
//		return ReportResponseDto.from(report);
//	}
//
//	// 관리자 처리 로그 조회
//	@Override
//	public List<ReportAuditLogDto> getReportAuditLogs(Long reportId) {
//		List<ReportAuditLog> reportAuditLogs = reportAuditLogRepository
//				.findByReport_ReportIdOrderByProcessedAtDesc(reportId);
//		
//		List<ReportAuditLogDto> logs = reportAuditLogs
//				.stream()
//				.map(ReportAuditLogDto::from)
//				.toList();
//		
//		return logs;
//	}
//
//	// 신고당한 회원 (신뢰도점수/뱃지) 조회
//	@Override
//	public MemberTrustInfoDto getMemberTrustInfo(Long targetMemberId) {
//		Member member = memberRepository
//				.findById(targetMemberId)
//				.orElseThrow(()-> new IllegalArgumentException("회원 member 조회 오류! targetMemberId: " + targetMemberId));
//		
//		MemberInfo memberInfo = memberInfoRepository
//				.findById(targetMemberId)
//				.orElseThrow(()-> new IllegalArgumentException("신고 대상 회원 MemberInfo 조회 오류! targetMemberId: " + targetMemberId));
//		
//		MemberTrustInfoDto memberInfoDto = new MemberTrustInfoDto();
//		memberInfoDto.setTargetMemberId(targetMemberId);			// 신고당한 회원 아이디
//		memberInfoDto.setTargetNickname(member.getNickname());		// 신고당한 회원 닉네임
//		
//		memberInfoDto.setTrustScore(memberInfo.getTrustScore());	// 신뢰도 점수
//		
//		if (memberInfo.getReportStatus() != null) {					// 뱃지 정보
//			MemberReportStatus reportStatus = memberInfo.getReportStatus();
//			memberInfoDto.setReportStatusId( reportStatus.getReportStatusId() );
//			memberInfoDto.setStatusCode( reportStatus.getStatusCode());
//			memberInfoDto.setStatusName( reportStatus.getStatusName());
//		}
//		
//		return memberInfoDto;
//	}
//
////	신고 처리 3일 후 만족도 조사 이메일 발송
//	@Override
//	public void sendThreeDaysAgoReportEmails() {
////	    for (Report report : reports) {
////	        String email = report.getMember().getEmail();	// 메일주소 조회
////	        
////	        if( email != null && !email.isEmpty() ) {
////				String subject = "[만족도 참여] Moit 문의 처리 결과는 어떠셨나요?";
////				String content = "Moit 문의 처리 결과는 어떠셨나요?"
////								+ "마음에 드셨다면 만족도 참여에 동참해주세요!"
////								+ "링크첨부...";
////
////				//메일 전송 test
////				try { apiEmail.sendMail(subject, content, email); }
////				catch (Exception e) { e.printStackTrace(); }
////
////	        } else { System.out.println("이메일이 없습니다. 메일 전송 실패..."); }
////	    }
//	}
//}
//
