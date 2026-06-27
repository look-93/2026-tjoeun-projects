package com.moit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.moit.dto.MeetupSearchDto;
import com.moit.service.AdminMeetupService;
import com.moit.util.PagingUtil;


@Controller
@RequestMapping("/meetup/admin")
public class AdminMeetupController {
	/*
	 * @RequestMapping( "/" ) public String index() { return
	 * "redirect:/meetup/admin/list.do"; }
	 */
	
	@Autowired AdminMeetupService adminMeetupService;
	
	
	@RequestMapping("/list.do")
	public String serchByAdmin(Model model, MeetupSearchDto meetupSerchDto, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
		model.addAttribute("menu", "meetup");
		model.addAttribute("paging", new PagingUtil(adminMeetupService.selectMeetupTotalCnt(meetupSerchDto), pstartno));
		model.addAttribute("serchList", adminMeetupService.searchByAdmin(pstartno,meetupSerchDto));
		return "meetup/admin/list"; 
	}
	
	@RequestMapping(value = "/delete.do",  method = RequestMethod.POST)
	public String deleteByAdmin(int meetupId) {
	
		adminMeetupService.deleteMeetup(meetupId);
		return "redirect:/meetup/admin/list.do";
	}	
	
}
