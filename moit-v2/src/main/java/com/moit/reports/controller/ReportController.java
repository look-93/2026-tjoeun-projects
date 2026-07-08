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

import com.moit.reports.api.ApiEmail;
import com.moit.reports.dto.ReportsDto;
import com.moit.reports.service.ReportsService;
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
								Principal principal) {
		
		int memberId = 1; // 신고자
		
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("start", 0);
//		map.put("end", 10);
//		map.put("memberId", memberId); // 로그인 회원 번호 test
		
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
		
		int memberId = 1;
		
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
	public String reportWrite_post(ReportsDto dto, RedirectAttributes rttr) {
		
		dto.setMemberId(1); // 로그인 회원 번호 test
		
		int result_TargetType = -1;
		String result = "신고등록 실패";

//		if ( "MEETUP".equals(dto.getTargetType()) ) {
//			result_TargetType = service.insertUserReport(dto);
//		} else if ( "REVIEW".equals(dto.getTargetType()) ) {
//			result_TargetType = service.insertUserReport(dto);
//		}
		if ("MEETUP".equals(dto.getTargetType()) || "REVIEW".equals(dto.getTargetType())) {
	        result_TargetType = service.insertUserReport(dto);
	    }
		
		if (result_TargetType > 0) {
			result = "신고등록 완료";
			rttr.addFlashAttribute("result", result);
			
			return "redirect:/user/meetup/report/mylist";
		}
		
		// 중복 신고 케이스 (서비스에서 -1을 보냈을 때)
	    if (result_TargetType == -1) {
	        result = "이미 신고 내역이 존재합니다.";
	    } else {
	        result = "신고 등록 중 오류가 발생했습니다. 다시 시도해주세요.";
	    }
	    
	    // 신고 작성 횟수 제한 (서비스에서 -2를 보냈을 때)
	    if (result_TargetType == -2) {
	    	result = "5회 이상의 신고 내역이 존재합니다. 다음 날 다시 시도해주세요.";
	    } else {
	    	result = "신고 등록 중 오류가 발생했습니다. 다시 시도해주세요.";
	    }
	    
		
    	rttr.addFlashAttribute("result", result);
    	return "redirect:/user/meetup/report/write?targetType=" + dto.getTargetType() + "&targetId=" + dto.getTargetId();
	}
	
	// 내 신고 상세 화면 detail
	@RequestMapping("/user/meetup/report/detail")
	public String reportDetail( int reportId, Model model) {
		
		ReportsDto dto = new ReportsDto();
		dto.setReportId(reportId);
		dto.setMemberId(1); // 신고자
		
		model.addAttribute("dto", service.selectUserReportDetail(dto));
		
		return "user/meetup/report/detail";
	}
	
	// 신고 수정 화면 update
	@GetMapping( value="/user/meetup/report/update")
	public String reportUpdate(int reportId, Model model) {

		ReportsDto dto = new ReportsDto();
		dto.setReportId(reportId);
		dto.setMemberId(1); // 신고자
		
		model.addAttribute("dto", service.selectUserReportDetail(dto));
		return "user/meetup/report/update";
	}
	
	// 신고 수정 처리
	@PostMapping("/user/meetup/report/update")
	public String reportUpdate_post(ReportsDto dto, RedirectAttributes rttr) {
		
		dto.setMemberId(1); // 신고자
		
		String result="신고수정 실패";
		
		if( service.updateUserReport(dto) > 0 ) {
			result="신고수정 완료";
		}

		rttr.addFlashAttribute("result", result);
		return "redirect:/user/meetup/report/detail?reportId=" + dto.getReportId();
	}
	
	// 신고 삭제 처리 delete
	@PostMapping("/user/meetup/report/delete")
	public String reportDelete_post(ReportsDto dto, RedirectAttributes rttr) {
		
		dto.setMemberId(1); // 신고자
		
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
							
							@RequestParam(value="searchType", required=false) String searchType,
							@RequestParam(value="keyword", required=false) String keyword,
							Model model) {
		
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

