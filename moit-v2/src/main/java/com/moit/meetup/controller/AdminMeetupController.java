package com.moit.meetup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/meetup")
public class AdminMeetupController {
	
	// 관리자 - 모임 리스트
	@GetMapping("/list")
	public String adminList() {
		return "admin/meetup/list";
	}
}
