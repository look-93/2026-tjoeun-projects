package com.moit.meetup.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.service.AdminMeetupService;
import com.moit.util.UtilPaging;



@Controller
@RequestMapping("/admin/meetup")
public class AdminMeetupController {
	
	@Autowired AdminMeetupService adminMeetupService;
	
	/*1. 관리자 - 모임 리스트 화면(HTML) 호출*/
	@GetMapping("/list")
	public String listPage() {
		// templates/admin/meetup/list.html (또는 설정하신 경로)을 열어줍니다.
		// ※ 본인의 실제 Thymeleaf 파일 경로 문자열을 리턴하셔야 합니다.
		return "/admin/meetup/list"; 
	}	
	
	/* 2. 관리자 - 모임 리스트 데이터(JSON) 호출 */
	@GetMapping("/list/data")
	@ResponseBody
	public Map<String, Object> listData(MeetupSearchDto meetupSearchDto){
		Integer pstartno = meetupSearchDto.getPstartno();
		
		// 처음 진입 시 pstartno가 0이거나 음수라면 기본값 1페이지로 설정
		if (pstartno == null || pstartno <= 0) {
		    pstartno = 1;
		    meetupSearchDto.setPstartno(1);
		}
	    //System.out.println("count = " + adminMeetupService.findAllMeetupCountBy(meetupSearchDto));
	    //System.out.println("list = " + adminMeetupService.findAllMeetupBy(pstartno, meetupSearchDto).size());
		Map<String, Object> map = new HashMap<>();
		map.put("menu", "meetup"); // 사이드바 메뉴 active 값
		map.put("paging", new UtilPaging(adminMeetupService.findAllMeetupCountBy(meetupSearchDto), pstartno) );
		map.put("searchList", adminMeetupService.findAllMeetupBy(pstartno, meetupSearchDto));
		return map;
	}
	
	// 관리자 - 모임 리스트 삭제
	@PostMapping("/delete")
	@ResponseBody
	public Map<String, Object> updateMeetupDeleteYn(@RequestBody Map<String, Integer> meetup){
		Map<String, Object> map = new HashMap<>();
		int meetupId = meetup.get("meetupId");
		int result = adminMeetupService.updateMeetupDeleteYn(meetupId);
		map.put("result", result);
		map.put("message", result > 0 ? "삭제 성공" : "삭제 실패");
			
		return map;		
	}
	
}
