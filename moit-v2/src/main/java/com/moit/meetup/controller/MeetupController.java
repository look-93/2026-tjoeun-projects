package com.moit.meetup.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.service.MeetupService;
import com.moit.util.UtilPaging;

@Controller
public class MeetupController {
	@Autowired MeetupService meetupService;
	
	/*1. 사용자 - 모임 리스트 화면(HTML) 호출*/
	@GetMapping("/meetup/list")
	public String listPage() {
		return "/user/meetup/list";
	}
	
	/*2. 사용자 - 모임 리스트 데이터(JSON) 호출*/
	@GetMapping("/meetup/list/data")
	@ResponseBody
	public Map<String, Object> listData(MeetupSearchDto meetupSearchDto){
		Integer pstartno = meetupSearchDto.getPstartno();
		
		if(pstartno == null || pstartno <= 0) {
			pstartno = 1;
			meetupSearchDto.setPstartno(1);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("paging", new UtilPaging(meetupService.findAllMeetupCountBy(meetupSearchDto), pstartno));
		map.put("searchList", meetupService.findAllMeetupBy(pstartno, meetupSearchDto));
		map.put("sidoList", meetupService.findAllSido());
		map.put("categoryList", meetupService.findAllCategory());
		System.out.println(meetupSearchDto.getSearchText());
		
		return map;
	}
	
}
