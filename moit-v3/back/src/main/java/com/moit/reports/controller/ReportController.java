package com.moit.reports.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.moit.member.repository.ReportStatusRepository;
import com.moit.reports.api.ApiEmail;
import com.moit.reports.api.ApiOpenAi;
import com.moit.reports.dto.ReportsDto;
import com.moit.reports.dto.ReportsDto.ReportRequestDto;
import com.moit.reports.dto.ReportsDto.ReportResponseDto;
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
	
	private final ReportRepository reportRepository;
	private final MemberReportStatusRepository memberReportStatusRepository;
	private final ReportAuditLogRepository reportAuditLogRepository;
	private final ReportsService reportsService;

	
	// test button
	@RequestMapping("/user/meetup/report/button")
    public String reportButton() {
        return "user/meetup/report/button";
    }
	
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
	
	// 내 신고내역 화면 mylist
	@RequestMapping("/user/meetup/report/mylist")
	public String reportMylist( @RequestParam(value="pstartno", defaultValue="1") int pstartno,
								HttpSession session,
								Model model,
								Authentication authentication) {
		

		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 	
		
		model.addAttribute("dto", user);
		model.addAttribute("paging", new UtilPaging( service.selectUserCnt(memberId), pstartno ));
		model.addAttribute("list", service.selectUserReport(pstartno, memberId));
		model.addAttribute("menu", "myReport");
		return "user/meetup/report/mylist";
	}
	
	
	// br등록
	@RequestMapping("/user/meetup/report/myPageMyReportList")
	public String myPageMyReport( @RequestParam(value="pstartno", defaultValue="1") int pstartno,
								HttpSession session,
								Model model,
								Authentication authentication) {
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 		
		
		model.addAttribute("paging", new UtilPaging( service.selectUserCnt(memberId), pstartno ));
		model.addAttribute("list", service.selectUserReport(pstartno, memberId));
		model.addAttribute("menu", "myReport");
		return "user/meetup/report/myPageMyReportList";
	}
	
	// 신고 작성
	@Operation(summary = "사용자 신고 작성", description = "")
	@PostMapping
	public ResponseEntity<ReportResponseDto> createReport(
		Authentication authentication,
		@Parameter(description = "작성자 ID") @RequestParam("memberId") Long memberId,
		@RequestBody ReportRequestDto requestDto ) {
		
		// 중복 검사 코드 추가
		
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
		return ResponseEntity.ok( reportsService.createUserReport(memberId , requestDto));
	};
	
	// 신고 삭제
	
	// 신고 삭제 처리 delete
	@PostMapping("/user/meetup/report/delete")
	public String reportDelete_post(ReportsDto dto, HttpSession session, RedirectAttributes rttr,
									Authentication authentication) {
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		}
//		Integer memberId = getLoginMemberId(session); // 사용자 login
		dto.setMemberId(memberId);
		
		String result="신고삭제 실패";
		
		if( service.deleteUserReport(dto) > 0 ) {
			result="신고삭제 성공";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/user/meetup/report/mylist";
	}
	
	// 내 신고 상세 화면 detail
	@RequestMapping("/user/meetup/report/detail")
	public String reportDetail( int reportId, HttpSession session, Model model,
								Authentication authentication) {
		
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		}
		
		ReportsDto dto = new ReportsDto();
		dto.setReportId(reportId);
		dto.setMemberId(memberId);
		
		
//		Integer memberId = getLoginMemberId(session); // 사용자 login
		
//		model.addAttribute("dto", service.selectUserReportDetail(dto));
		ReportsDto detail = service.selectUserReportDetail(dto);
		model.addAttribute("dto", detail);
		
		return "user/meetup/report/detail";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
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
		
//		Integer memberId = getLoginAdminId(session);
	
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
		
//		Integer memberId = (Integer) session.getAttribute("loginMemberId");
//		if (memberId == null) { memberId = 12; }
		
//		Integer memberId = getLoginAdminId(session);
		
		HashMap<String, Object> map = new HashMap<>(); // 조회 조건
		map.put("reportId", reportId);
//		model.addAttribute("dto", service.selectAdminReports(map));
		
		List<ReportsDto> list = service.selectAdminReports(map); // 관리자 상세 조회 결과
		if (list != null && !list.isEmpty()) {
			model.addAttribute("dto", list.get(0));
		}
		
		return "admin/report/adminDetail";
	}
	
	// 관리자 APPROVED 수정
	@PostMapping("/admin/report/update")
	public String reportUpdateAdmin_post(ReportsDto dto, HttpSession session, RedirectAttributes rttr) {
		
//		Integer memberId = getLoginAdminId(session);
		
		String result="status 상태 수정 실패";
		
		if( service.updateAdmin(dto) > 0 ) {
			if( "APPROVED".equals(dto.getStatus()) ) {
				result="APPROVED 수정 성공";
			}
			
			else if ( "REJECTED".equals(dto.getStatus()) ) {
				result="REJECTED 수정 성공";
			}
		}
		rttr.addFlashAttribute("result", result);
		return "redirect:/admin/report/adminDetail?reportId=" + dto.getReportId();
	}
	
	// 관리자 신고 삭제
	@PostMapping("/admin/report/delete")
	public String reportDeleteAdmin_post(	@RequestParam("reportId") int reportId,
											ReportsDto dto, HttpSession session, RedirectAttributes rttr) {
		
//		Integer memberId = getLoginAdminId(session);
		
		String result="신고삭제 실패";
		
		if( service.deleteAdmin(reportId) > 0 ) {
			result="신고삭제 성공";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/admin/report/adminList";
	}
	
	
	//////////////////////////////////////////////////////////
	// open ai
	
	@GetMapping("/report/api/openai")
	public String openai_get() {
		return "";
	}
	
	@PostMapping(value = "/report/api/openai",
				produces = "text/plain; charset=UTF-8")
//				produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String openai_post( @RequestBody String keywords ) {
		
		System.out.println("AI Controller 도착");
	    System.out.println("전달받은 값: " + keywords);

	    String result = apiOpenAi.getAIResponse(keywords);

	    System.out.println("AI 결과: " + result);
	    
		return apiOpenAi.getAIResponse(keywords);
	}
	
	
}

