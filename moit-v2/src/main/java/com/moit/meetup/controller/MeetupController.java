package com.moit.meetup.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moit.meetup.dto.MeetupApplicationDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupLikeDto;
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
		//System.out.println(meetupSearchDto.getCategoryId());
		Map<String, Object> map = new HashMap<>();
		map.put("paging", new UtilPaging(meetupService.findAllMeetupCountBy(meetupSearchDto), pstartno));
		map.put("searchList", meetupService.findAllMeetupBy(pstartno, meetupSearchDto));
		map.put("sidoList", meetupService.findAllSido());
		map.put("categorys", meetupService.findAllCategory());
		//System.out.println(meetupSearchDto.getSearchText());
		
		return map;
	}
	
	/* 사용자 - 좋아요 기능*/
	@PostMapping("/meetup/list/like")
	@ResponseBody
	public Map<String, Object> meetupLike(MeetupLikeDto meetupLikeDto, Authentication authentication, MeetupDto meetupdto) {
		
		/*
		 * CustomUser user = (CustomUser) authentication.getPrincipal(); int memberId =
		 * userMeetupService.findByMamberId(user.getUsername());
		 * meetupLikeDto.setMemberId(memberId);
		 */	
		meetupLikeDto.setMemberId(1);
		Map<String, Object> result = new HashMap<>();
		
		//meetupLikeDto.setMemberId(3);
		boolean hasLike = meetupService.insertMeetupLike(meetupLikeDto);	
		result.put("hasLike", hasLike);
	    result.put("likeCnt", meetupService.countMeetupLike(meetupLikeDto));
		return result;	
	}	
	
	/*사옹자 - 모임상세조회*/
	@GetMapping("/meetup/detail")
	public String detail(Model model, Authentication authentication, MeetupApplicationDto meetupApplicationDto) {
		
//		CustomUser user = (CustomUser) authentication.getPrincipal(); 
//		int memberId = userMeetupService.findByMamberId(user.getUsername());
//		meetupApplicationsDto.setMemberId(memberId);
		
		meetupApplicationDto.setMemberId(2);
		
		meetupApplicationDto.setStatusList(Arrays.asList("PENDING", "APPROVED"));
		model.addAttribute("applyInfo",meetupService.findApplyInfo(meetupApplicationDto));
		model.addAttribute("detail", meetupService.selectMeetupDetail(meetupApplicationDto.getMeetupId()));
		System.out.println(meetupService.selectMeetupDetail(meetupApplicationDto.getMeetupId()));
		
		return "user/meetup/detail";
	}
	
	////////////////모집신청////////////////////
	//모집신청
	@PostMapping("/meetup/applyMeetup")
	@ResponseBody
	public Map<String, Object> applyMeetup(@RequestBody MeetupApplicationDto  meetupApplicationDto, MeetupDto meetupdto, Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
//		CustomUser user = (CustomUser) authentication.getPrincipal();		
//		int memberId = userMeetupService.findByMamberId(user.getUsername());		
//		meetupApplicationsDto.setMemberId(memberId);
		meetupApplicationDto.setMemberId(2);

		boolean result = meetupService.insertApplication(meetupApplicationDto ) > 0;
		map.put("result", result);
		return map;
	}
		

	//모집신청취소
	@PostMapping("/meetup/cancelApplyMeetup")
	@ResponseBody
	public Map<String, Object> cancelApplyMeetup(@RequestBody MeetupApplicationDto  meetupApplicationDto, MeetupDto meetupdto, Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
//		CustomUser user = (CustomUser) authentication.getPrincipal();		
//		int memberId = userMeetupService.findByMamberId(user.getUsername());		
//		meetupApplicationsDto.setMemberId(memberId);
		meetupApplicationDto.setMemberId(2);

		boolean result = meetupService.cancelApplyMeetup(meetupApplicationDto ) > 0;
		map.put("result", result);
		return map;
	}	
	
}
