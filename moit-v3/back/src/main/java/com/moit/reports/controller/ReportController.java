package com.moit.reports.controller;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moit.member.dto.UserDto;
import com.moit.member.repository.ReportStatusRepository;
import com.moit.reports.api.ApiOpenAi;
import com.moit.reports.dto.ReportsDto;
import com.moit.reports.dto.ReportSearchDto;
import com.moit.reports.dto.ReportsDto.ReportListResponseDto;
import com.moit.reports.dto.ReportsDto.ReportProcessDto;
import com.moit.reports.dto.ReportsDto.ReportRequestDto;
import com.moit.reports.dto.ReportsDto.ReportResponseDto;
import com.moit.reports.service.ReportsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Reports Api", description = "신고 관련 API")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
	
	private final ReportsService reportsService;

	
	// test button
	@RequestMapping("/user/meetup/report/button")
    public String reportButton() {
        return "user/meetup/report/button";
    }
	
	// 사용자 로그인 헬퍼
//	private Long getLoginMemberId(Authentication authentication) {
//		Integer id = (Integer) session.getAttribute("loginMemberId");
//		return (id != null) ? id : 1; // 일반회원 테스트용
//	}

	// 사용자 로그인 헬퍼
//	private Integer getLoginMemberId(HttpSession session) {
//		Integer id = (Integer) session.getAttribute("loginMemberId");
//		return (id != null) ? id : 1; // 일반회원 테스트용
//	}
	// 관리자 로그인 헬퍼
//	private Integer getLoginAdminId(HttpSession session) {
//		Integer id = (Integer) session.getAttribute("loginMemberId");
//		return (id != null) ? id : 22; // 관리자 테스트용
//	}
	

	
	// 신고 작성
	@Operation(summary = "사용자 신고 작성", description = "")
	@PostMapping
	public ResponseEntity<ReportResponseDto> createReport(
			Authentication authentication,
			@Parameter(description = "작성자 ID") @RequestParam("memberId") Long memberId,
			@RequestBody ReportRequestDto requestDto ) {
		
		// 로그인한 memberId 꺼내오기
//		Long memberId = authUserJwtService.getCurrentMemberId(authentication);
		
		return ResponseEntity.ok( reportsService.createUserReport(memberId, requestDto));
	};
	
	// 신고 수정
	@Operation(summary = "사용자 신고 수정", description = "")
	@PatchMapping(value = "/{reportId}")
	public ResponseEntity<ReportResponseDto> updateReport(
			Authentication authentication,
			@Parameter(description = "수정할 신고글 ID") @PathVariable(name = "reportId") Long reportId,
			@RequestBody ReportRequestDto requestDto ) {
		
		// 로그인 하드코딩
		Long memberId = 1L;
		
		// 로그인한 memberId 꺼내오기
//		Long memberId =  authUserJwtService.getCurrentMemberId(authentication);
		
		ReportResponseDto response = reportsService.updateUserReport(reportId, memberId, requestDto);
		
		return ResponseEntity.ok(response);
	};
	
	// 신고 삭제
	@Operation(summary = "사용자 신고 삭제", description = "")
	@DeleteMapping("/{reportId}")
	public ResponseEntity<Long> deleteReport(
			Authentication	authentication,
			@PathVariable("reportId") Long reportId ) {
		
		// 로그인 하드코딩
		Long memberId = 1L;
		
		// 로그인한 memberId 꺼내오기
//		Long memberId = authUserJwtService.getCurrentMemberId(authentication);
		
		reportsService.deleteUserReport(reportId, memberId);
		return ResponseEntity.ok(reportId);
	}
	
	// 내 신고내역 조회 (사용자 신고 목록 조회 + 페이징)
	@Operation(summary = "내 신고내역 조회", description = "")
	@GetMapping
	public ResponseEntity<ReportListResponseDto> getReportsMylist (
			Authentication authentication, 
			@Parameter(description = "작성자 ID") @RequestParam("memberId") Long memberId,
			@PageableDefault(
		            size = 10,							// 한 페이지에 10개
		            sort = "reportId",					// reportId
		            direction = Sort.Direction.DESC		// 내림차순 조회
		        ) Pageable pageable ) {
		
		// 로그인한 memberId 꺼내오기
//		Long memberId = authUserJwtService.getCurrentMemberId(authentication);
		
		ReportListResponseDto response = reportsService.getUserReports(memberId, pageable);
		return ResponseEntity.ok(response);
	}
	
	// 사용자 신고 상세 조회
	@Operation(summary = "내 신고내역 상세조회", description = "")
	@GetMapping("/{reportId}")
	public ResponseEntity<ReportResponseDto> getReportMylistDetail (
			@PathVariable("reportId") Long reportId) {
		
		// 로그인 하드코딩
		Long memberId = 1L;
		
		// 로그인한 memberId 꺼내오기
//		Long memberId = authUserJwtService.getCurrentMemberId(authentication);
		
		ReportResponseDto report = reportsService.getUserReportDetail(reportId, memberId);
		return ResponseEntity.ok(report);
	}
	
	
	
	////////////////////////////////////////////////////////////////
	// 관리자 신고 수정
	@Operation(summary = "관리자 신고 처리 (승인/반려)", description = "")
	@PatchMapping(value = "/admin/{reportId}")
	public ResponseEntity<Long> updateAdminReport(
			Authentication authentication,
			@Parameter(description = "처리할 신고글 ID") @PathVariable(name = "reportId") Long reportId,
			@RequestParam ReportProcessDto processDto ) {
		
		// 관리자 로그인 하드코딩
		Long MemberId = 99L;
		
		// 로그인한 adminMemberId 꺼내오기
//		Long adminMemberId =  authUserJwtService.getCurrentMemberId(authentication);
		
		reportsService.updateAdminReport(reportId, MemberId, processDto);
		return ResponseEntity.ok(reportId);
	};
	
	// 관리자 신고 삭제
	@Operation(summary = "관리자 신고 삭제", description = "")
	@DeleteMapping("/admin/{reportId}")
	public ResponseEntity<Long> deleteAdminReport(
			Authentication	authentication,
			@PathVariable("reportId") Long reportId,
			@RequestParam("processReason") String processReason ) {
		
		// 로그인 하드코딩
		Long adminMemberId = 99L;
		
		// 로그인한 adminMemberId 꺼내오기
//		Long adminMemberId = authUserJwtService.getCurrentMemberId(authentication);
		
		reportsService.deleteAdminReport(reportId, adminMemberId, processReason);
		return ResponseEntity.ok(reportId);
	}
	
	// 관리자 신고 목록 조회 + 검색 + 페이징
	@Operation(summary = "관리자 신고 목록 조회", description = "신고 상태, 삭제 여부, 대상 유형, 작성자(닉네임), 신고 사유로 검색합니다.")
	@GetMapping("/admin/adminReportsList")
	public ResponseEntity<ReportListResponseDto> getReportsAdmin (
			Authentication	authentication,
			@ModelAttribute ReportSearchDto searchDto,	// 검색조건
			@PageableDefault(							// 페이지 정보
					size = 10,
					sort = "reportId",
					direction = Sort.Direction.DESC ) Pageable pageable ) {
		
		// 검색 기능 추가 ReportSearchDto
		// 신고 상태
		if (searchDto.getStatus() != null) {
			
		}
			
		// 삭제 여부
		// 대상 유형
		// 작성자(닉네임)
		// 신고 사유
		
		ReportListResponseDto response = reportsService.getAdminReports(searchDto, pageable);
		return ResponseEntity.ok(response);
	}
	
	// 관리자 리스트 상세보기
	@Operation(summary = "관리자 리스트 상세 조회", description = "관리자 리스트를 상세 조회합니다.")
	@GetMapping("/admin/{reportId}")
	public ResponseEntity<ReportResponseDto> getReportAdminDetail(
			@PathVariable("reportId") Long reportId) {
		
		ReportResponseDto report = reportsService.getAdminReportDetail(reportId);
		return ResponseEntity.ok(report);
	}
	
	
	//////////////////////////////////////////////////////////
	// open ai
//	
//	@GetMapping("/report/api/openai")
//	public String openai_get() {
//		return "";
//	}
//	

//	@PostMapping(value = "/report/api/openai", produces = "text/plain; charset=UTF-8")
//	@ResponseBody
//	public String openai_post( @RequestBody String keywords ) {
//		
//		System.out.println("AI Controller 도착");
//	    System.out.println("전달받은 값: " + keywords);
//
//	    String result = apiOpenAi.getAIResponse(keywords);
//
//	    System.out.println("AI 결과: " + result);
//	    
//		return apiOpenAi.getAIResponse(keywords);
//	}

}
