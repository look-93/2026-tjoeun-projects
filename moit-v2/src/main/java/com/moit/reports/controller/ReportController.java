package com.moit.reports.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.member.dto.UserDto;
import com.moit.reports.api.ApiEmail;
import com.moit.reports.dto.ReportsDto;
import com.moit.reports.service.ReportsService;
import com.moit.security.CustomUserDetails;
import com.moit.util.UtilPaging;

@Controller
public class ReportController {
	@Autowired ReportsService service;
	@Autowired ApiEmail apiEmail;
	
	// test button
	@RequestMapping("/user/meetup/report/button")
    public String reportButton() {
        return "user/meetup/report/button";
    }
	
	// 내 신고내역 화면 mylist
	@RequestMapping("/user/meetup/report/mylist")
	public String reportMylist( @RequestParam(value="pstartno", defaultValue="1") int pstartno,
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

		
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("start", 0);
//		map.put("end", 10);
//		map.put("memberId", memberId); // 로그인 회원 번호 test
		
//		model.addAttribute("paging", new PagingUtil( service.selectUserCnt(dto.getMemberId()), pstartno ));
		model.addAttribute("paging", new UtilPaging( service.selectUserCnt(memberId), pstartno ));
		model.addAttribute("list", service.selectUserReport(pstartno, memberId));
		model.addAttribute("menu", "myReport");
		return "user/meetup/report/mylist";
	}
	
	
	// br등록
	@RequestMapping("/user/meetup/report/myPageMyReportList")
	public String myPageMyReport( @RequestParam(value="pstartno", defaultValue="1") int pstartno,
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
	// 신고 작성 화면 write
	@GetMapping("/user/meetup/report/write")
	public String reportWrite(	@RequestParam("targetType") String targetType,
								@RequestParam("targetId") int targetId,
								Model model) {
		
		ReportsDto dto = new ReportsDto();
		dto.setTargetType(targetType);
		dto.setTargetId(targetId);
		
		model.addAttribute("dto", dto);
		
		return "user/meetup/report/write";
	}
	// 신고 작성 기능
	@PostMapping("/user/meetup/report/write")
	public String reportWrite_post(ReportsDto dto, RedirectAttributes rttr, Authentication authentication) {
		
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
				
		
		dto.setMemberId(memberId); // 로그인 회원 번호 test
		
		int result_TargetType = -1;
		String result = "신고등록 실패";

		if ( "MEETUP".equals(dto.getTargetType()) ) {
			result_TargetType = service.insertUserReport(dto);
		} else if ( "REVIEW".equals(dto.getTargetType()) ) {
			result_TargetType = service.insertUserReport(dto);
		}
		
		if (result_TargetType > 0) {
			result = "신고등록 완료";
			rttr.addFlashAttribute("result", result);
			
			return "redirect:/user/meetup/report/mylist";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/user/meetup/report/write?targetType=" + dto.getTargetType() + "&targetId=" + dto.getTargetId();
	}
	
	// 내 신고 상세 화면 detail
	@RequestMapping("/user/meetup/report/detail")
	public String reportDetail( int reportId, Model model,Authentication authentication) {
		
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
		
		model.addAttribute("dto", service.selectUserReportDetail(dto));
		
		return "user/meetup/report/detail";
	}
	
	// 신고 수정 화면 update
	@GetMapping( value="/user/meetup/report/update")
	public String reportUpdate(int reportId, Model model, Authentication authentication) {
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
		
		model.addAttribute("dto", service.selectUserReportDetail(dto));
		return "user/meetup/report/update";
	}
	
	// 신고 수정 처리
	@PostMapping("/user/meetup/report/update")
	public String reportUpdate_post(ReportsDto dto, RedirectAttributes rttr, Authentication authentication) {
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
		dto.setMemberId(memberId);
		
		String result="신고수정 실패";
		
		if( service.updateUserReport(dto) > 0 ) {
			result="신고수정 완료";
		}

		rttr.addFlashAttribute("result", result);
		return "redirect:/user/meetup/report/detail?reportId=" + dto.getReportId();
	}
	
	// 신고 삭제 처리 delete
	@PostMapping("/user/meetup/report/delete")
	public String reportDelete_post(ReportsDto dto, RedirectAttributes rttr, Authentication authentication) {
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
		dto.setMemberId(memberId); 
		
		String result="신고삭제 실패";
		
		if( service.deleteUserReport(dto) > 0 ) {
			result="신고삭제 성공";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/user/meetup/report/mylist";
	}
	
	
	// 관리자 리스트 목록
	@GetMapping("/admin/report/adminList")
	public String adminList(@RequestParam(value="pstartno", defaultValue="1") int pstartno,
							@RequestParam(value="targetType", required=false) String targetType,
							@RequestParam(value="status", required=false) String status,
							@RequestParam(value="deleteYn", required=false) String deleteYn,
							Model model) {
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("targetType", targetType);
		map.put("status", status);
		map.put("deleteYn", deleteYn);
		
		map.put("start", (pstartno-1)*10);
		map.put("end", 10);
		
		model.addAttribute("menu", "report");
		model.addAttribute("paging", new UtilPaging( service.selectAdminReportsCnt(map), pstartno));
		model.addAttribute("list", service.selectAdminReports(map));
		
		model.addAttribute("targetType", targetType); // meetup, review
		model.addAttribute("status", status); // pendding
		model.addAttribute("deleteYn", deleteYn); // delete

		return "admin/report/adminList";
	}
	
	// 관리자 리스트 목록 상세보기
	@RequestMapping("/admin/report/adminDetail")
	public String adminDetail(	@RequestParam("reportId") int reportId,
								Model model) {
		
		HashMap<String, Object> map = new HashMap<>(); // 조회 조건
		map.put("reportId", reportId);
//		model.addAttribute("dto", service.selectAdminReports(map));
		
		List<ReportsDto> list = service.selectAdminReports(map); // 조회 결과
		if (list != null && !list.isEmpty()) {
			model.addAttribute("dto", list.get(0));
		}
		
		return "admin/report/adminDetail";
	}
	
	// 관리자 APPROVED 수정
	@PostMapping("/admin/report/update")
	public String reportUpdateAdmin_post(ReportsDto dto, RedirectAttributes rttr) {
		
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
		return "redirect:/admin/report/adminList";
	}
	
	// 관리자 신고 삭제
	@PostMapping("/admin/report/delete")
	public String reportDeleteAdmin_post(	@RequestParam("reportId") int reportId,
											ReportsDto dto, RedirectAttributes rttr) {
		String result="신고삭제 실패";
		
		if( service.deleteAdmin(reportId) > 0 ) {
			result="신고삭제 성공";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/admin/report/adminList";
	}
	
	
	
	
	
}

