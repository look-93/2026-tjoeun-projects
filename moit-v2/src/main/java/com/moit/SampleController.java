package com.moit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {
	// tmptAdmin 복사해서 content안에 본인 html 붙여넣으면됩니다
	
	// 탬플릿 샘플
	@GetMapping("/tmptAdmin")
	public String tmptAdmin() {return "/admin/temp/tmptAdmin"; } //prefix(/templates) + tmpt + suffix(.html)
	
	@GetMapping("/tmptMain")
	public String tmptMain() {return "/user/temp/tmptMain"; }

	@GetMapping("/tmptMeetupList")
	public String tmptMeetupList() {return "/user/temp/tmptMeetupList"; }
	
	@GetMapping("/tmptMeetupDetail")
	public String tmptMeetupDetail() {return "/user/temp/tmptMeetupDetail"; }	
	
	@GetMapping("/tmptMypage")
	public String tmptMypage() {return "/user/temp/tmptMypage"; }

	// admin 페이지 샘플 
	@GetMapping("/sample/admin")
	public String admin() {return "/sample/admin"; }

	// main 페이지 샘플 
	@GetMapping("/sample/main")
	public String main() {return "/sample/main"; }

	// meetupList 페이지 샘플 
	@GetMapping("/sample/meetupList")
	public String meetupList() {return "/sample/meetupList"; }
	
	// meetupDetail 페이지 샘플 
	@GetMapping("/sample/meetupDetail")
	public String meetupDetail() {return "/sample/meetupDetail"; }	
	
	// mypage 페이지 샘플 
	@GetMapping("/sample/mypage")
	public String mypage() {return "/sample/mypage"; }		
	
	
}
