package com.moit.reports.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.member.dto.UserDto;
//import com.moit.member.repository.ReportStatusRepository;
import com.moit.reports.api.ApiOpenAi;
import com.moit.reports.dto.ReportsDto;
import com.moit.reports.dto.ReportsDto.ReportListResponseDto;
import com.moit.reports.dto.ReportsDto.ReportProcessDto;
import com.moit.reports.dto.ReportsDto.ReportRequestDto;
import com.moit.reports.dto.ReportsDto.ReportResponseDto;
import com.moit.reports.entity.Report;
import com.moit.reports.repository.MemberReportStatusRepository;
import com.moit.reports.repository.ReportAuditLogRepository;
import com.moit.reports.repository.ReportRepository;
import com.moit.reports.service.ReportsService;
import com.moit.security.CustomUserDetails;
import com.moit.util.UtilPaging;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
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
	public ResponseEntity<ReportListResponseDto> getAllReportMylist(
			Authentication authentication, 
			@Parameter(description = "작성자 ID") @RequestParam("memberId") Long memberId ) {
		
		// 로그인한 memberId 꺼내오기
//		Long memberId = authUserJwtService.getCurrentMemberId(authentication);
		
		return ResponseEntity.ok( reportsService.getUserReports(memberId, null) );
	}
	
	// 사용자 신고 상세 조회
	@Operation(summary = "내 신고내역 상세조회", description = "")
	@GetMapping("/{reportId}")
	public ResponseEntity<ReportResponseDto> selectPost(
			@PathVariable("reportId") Long reportId) {
		
		// 로그인 하드코딩
		Long memberId = 1L;
		
		// 로그인한 memberId 꺼내오기
//		Long memberId = authUserJwtService.getCurrentMemberId(authentication);
		
		Report report = reportsService.getUserReportDetail(reportId, memberId);
		return ResponseEntity.ok( new ReportResponseDto(post) );	// 200
	}
	
	
	
	////////////////////////////////////////////////////////////////
	// 관리자 신고 수정
	@Operation(summary = "관리자 신고 처리 (승인/반려)", description = "")
	@PatchMapping(value = "/{reportId}")
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
	
	// 신고 삭제
	@Operation(summary = "관리자 신고 삭제", description = "")
	@DeleteMapping("/{reportId}")
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
	
	
	// 관리자 리스트 목록
	@GetMapping("/admin/report/adminList")
	public String adminList(@RequestParam(value="pstartno", defaultValue="1") int pstartno,
							@RequestParam(value="targetType", required=false) String targetType,
							@RequestParam(value="status", required=false) String status,
							@RequestParam(value="deleteYn", required=false) String deleteYn,
							
							@RequestParam(value="searchType", required=false) String searchType,
							@RequestParam(value="keyword", required=false) String keyword,
							HttpSession session,
							Model model) {
		
//			Integer memberId = getLoginAdminId(session);
	
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("targetType", targetType);
		map.put("status", status);
		map.put("deleteYn", deleteYn);

		map.put("searchType", searchType);
		map.put("keyword", keyword);
		
		map.put("start", (pstartno-1)*10);
		map.put("end", 10);
		
		model.addAttribute("menu", "report");
		model.addAttribute("paging", new UtilPaging( service.selectAdminReportsCnt(map), pstartno));
		model.addAttribute("list", service.selectAdminReports(map));
		
		model.addAttribute("targetType", targetType); // meetup, review
		model.addAttribute("status", status); // pendding
		model.addAttribute("deleteYn", deleteYn); // delete

		if( keyword != null ) {
			keyword = keyword.trim();
		}
		model.addAttribute("searchType", searchType); // 검색 옵션
		model.addAttribute("keyword", keyword); // 작성자, 사유, 날짜
		

		return "admin/report/adminList";
	}
	
	// 관리자 리스트 목록 상세보기
	@GetMapping("/admin/report/adminDetail")
	public String adminDetail(	@RequestParam("reportId") int reportId,
								HttpSession session,
								Model model) {
		
//			Integer memberId = (Integer) session.getAttribute("loginMemberId");
//			if (memberId == null) { memberId = 12; }
		
//			Integer memberId = getLoginAdminId(session);
		
		HashMap<String, Object> map = new HashMap<>(); // 조회 조건
		map.put("reportId", reportId);
//			model.addAttribute("dto", service.selectAdminReports(map));
		
		List<ReportsDto> list = service.selectAdminReports(map); // 관리자 상세 조회 결과
		if (list != null && !list.isEmpty()) {
			model.addAttribute("dto", list.get(0));
		}
		
		return "admin/report/adminDetail";
	}
	
	
	//////////////////////////////////////////////////////////
	// open ai
//	
//	@GetMapping("/report/api/openai")
//	public String openai_get() {
//		return "";
//	}
//	
//	@PostMapping(value = "/report/api/openai",
//				produces = "text/plain; charset=UTF-8")
////				produces = MediaType.APPLICATION_JSON_VALUE)
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
//	
	
}

