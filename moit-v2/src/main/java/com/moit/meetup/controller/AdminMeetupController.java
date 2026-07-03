package com.moit.meetup.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.service.AdminMeetupService;
import com.moit.util.UtilPaging;

@Controller
@RequestMapping("/admin/meetup")
public class AdminMeetupController {
	
	@Autowired AdminMeetupService adminMeetupService;
	
	// 관리자 - 모임 리스트
	@GetMapping("/list")
	@ResponseBody
	public Map<String, Object> adminList(MeetupSearchDto meetupSearchDto, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
		Map<String, Object> map = new HashMap<>();
		map.put("menu", "meetup"); // 사이드바 메뉴 active 값
		map.put("paging", new UtilPaging(adminMeetupService.findAllMeetupCountBy(meetupSearchDto), pstartno) );
		map.put("searchList", adminMeetupService.findAllMeetupBy(pstartno, meetupSearchDto));
		return map;
	}
}
