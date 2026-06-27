package com.moit.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.dto.ReportsDto;
import com.moit.service.ReportsService;
import com.moit.util.PagingUtil;

@Controller
public class ReportController { // test lcy
	@Autowired ReportsService service;
	
	// 내 신고내역 화면 mylist
	@RequestMapping("/report/user/mylist.do")
	public String reportMylist( @RequestParam(value="pstartno", defaultValue="1") int pstartno,
								Model model,
								Principal principal) {
		
		int memberId = 1;
		
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("start", 0);
//		map.put("end", 10);
//		map.put("memberId", memberId); // 로그인 회원 번호 test
		
//		model.addAttribute("paging", new PagingUtil( service.selectUserCnt(dto.getMemberId()), pstartno ));
		model.addAttribute("paging", new PagingUtil( service.selectUserCnt(memberId), pstartno ));
		model.addAttribute("list", service.selectUserReport(pstartno, memberId));
		model.addAttribute("menu", "myReport");
		return "report/user/mylist";
	}
	
	
	// br등록
	@RequestMapping("/report/user/myPageMyReportList.do")
	public String myPageMyReport( @RequestParam(value="pstartno", defaultValue="1") int pstartno,
								Model model,
								Authentication authentication) {
		
		int memberId = 1;
		
		model.addAttribute("paging", new PagingUtil( service.selectUserCnt(memberId), pstartno ));
		model.addAttribute("list", service.selectUserReport(pstartno, memberId));
		model.addAttribute("menu", "myReport");
		return "report/user/myPageMyReportList";
	}
	// 신고 작성 화면 write
	@RequestMapping( value="/report/user/write.do", method = RequestMethod.GET  )
	public String reportWrite(	@RequestParam("targetType") String targetType,
								@RequestParam("targetId") int targetId,
								Model model) {
		
		ReportsDto dto = new ReportsDto();
		dto.setTargetType(targetType);
		dto.setTargetId(targetId);
		
		model.addAttribute("dto", dto);
		
		return "report/user/write";
	}
	// 신고 작성 기능
	@RequestMapping( value="/report/user/write.do", method = RequestMethod.POST  )
	public String reportWrite_post(ReportsDto dto, RedirectAttributes rttr) {
		
		dto.setMemberId(1); // 로그인 회원 번호 test
		
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
			
			return "redirect:/report/user/mylist.do";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/report/user/write.do?targetType=" + dto.getTargetType() + "&targetId=" + dto.getTargetId();
	}
	
	// 내 신고 상세 화면 detail
	@RequestMapping("/report/user/detail.do")
	public String reportDetail( int reportId, Model model) {
		
		ReportsDto dto = new ReportsDto();
		dto.setReportId(reportId);
		dto.setMemberId(1);
		
		model.addAttribute("dto", service.selectUserReportDetail(dto));
		
		return "report/user/detail";
	}
	
	// 신고 수정 화면 update
	@RequestMapping( value="/report/user/update.do", method = RequestMethod.GET  )
	public String reportUpdate(int reportId, Model model) {

		ReportsDto dto = new ReportsDto();
		dto.setReportId(reportId);
		dto.setMemberId(1);
		
		model.addAttribute("dto", service.selectUserReportDetail(dto));
		return "report/user/update";
	}
	
	// 신고 수정 처리
	@RequestMapping( value="/report/user/update.do", method = RequestMethod.POST )
	public String reportUpdate_post(ReportsDto dto, RedirectAttributes rttr) {
		
		String result="신고수정 실패";
		
		if( service.updateUserReport(dto) > 0 ) {
			result="신고수정 완료";
		}

		rttr.addFlashAttribute("result", result);
		return "redirect:/report/user/detail.do?reportId=" + dto.getReportId();
	}
	
	// 신고 삭제 처리 delete
	@RequestMapping( value="/report/user/delete.do", method=RequestMethod.POST )
	public String reportDelete_post(ReportsDto dto, RedirectAttributes rttr) {
		
		dto.setMemberId(1); 
		
		String result="신고삭제 실패";
		
		if( service.deleteUserReport(dto) > 0 ) {
			result="신고삭제 성공";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/report/user/mylist.do";
	}
	
	
	// 관리자 리스트 목록
	@RequestMapping("/report/admin/adminList.do")
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
		model.addAttribute("paging", new PagingUtil( service.selectAdminReportsCnt(map), pstartno));
		model.addAttribute("list", service.selectAdminReports(map));

		return "report/admin/adminList";
	}
	
	// 관리자 리스트 목록 상세보기
	@RequestMapping("/report/admin/adminDetail.do")
	public String adminDetail(	@RequestParam("reportId") int reportId,
								Model model) {
		
		HashMap<String, Object> map = new HashMap<>(); // 조회 조건
		map.put("reportId", reportId);
//		model.addAttribute("dto", service.selectAdminReports(map));
		
		List<ReportsDto> list = service.selectAdminReports(map); // 조회 결과
		if (list != null & !list.isEmpty()) {
			model.addAttribute("dto", list.get(0));
		}
		
		return "report/admin/adminDetail";
	}
	
	// 관리자 APPROVED 수정
	@RequestMapping( value="/report/admin/update.do", method=RequestMethod.POST )
	public String reportUpdateAdmin_post(ReportsDto dto, RedirectAttributes rttr) {
		
		String result="APPROVED 수정 실패";
		
		if( service.updateAdmin(dto) > 0 ) {
			result="APPROVED 수정 성공";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/report/admin/adminList.do";
	}
	
	// 관리자 신고 삭제
	@RequestMapping( value="/report/admin/delete.do", method=RequestMethod.POST )
	public String reportDeleteAdmin_post(	@RequestParam("reportId") int reportId,
											ReportsDto dto, RedirectAttributes rttr) {
		
		String result="신고삭제 실패";
		
		if( service.deleteAdmin(reportId) > 0 ) {
			result="신고삭제 성공";
		}
		
		rttr.addFlashAttribute("result", result);
		return "redirect:/report/admin/adminList.do";
	}
	
}

