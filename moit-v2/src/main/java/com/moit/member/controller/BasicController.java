package com.moit.member.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/user")
public class BasicController {
	
	// 메인페이지
	@GetMapping("/main")
	public String index() {  return "user/main"; }
	
}
