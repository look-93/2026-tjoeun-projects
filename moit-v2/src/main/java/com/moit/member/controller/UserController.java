package com.moit.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.member.dto.UserDto;
import com.moit.member.service.UserService;
import com.moit.security.CustomUserDetails;


@Controller
@RequestMapping("/user/member")
public class UserController {

	@Autowired UserService service;
	
//	// 메인페이지
//	@GetMapping("/")
//	public String index() {  return "/main"; }
	
	// 회원가입
	@GetMapping("/join")
	public String join() {  return "user/member/join"; }
		
	@PostMapping("/join")
	public String join_post(UserDto dto, RedirectAttributes rttr) {  
		
		int result = service.insert(dto);
		
		if(result==1) {
			rttr.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
			return "redirect:/user/member/login"; 
		}
		else if(result==0) {
			rttr.addFlashAttribute("msg", "이미 사용중인 아이디입니다.");
			return "redirect:/user/member/join"; 
		}
		else if(result==-1){
			rttr.addFlashAttribute("msg", "이미 사용중인 닉네임입니다.");
			return "redirect:/user/member/join"; 
		}
		
		rttr.addFlashAttribute("msg", "회원가입에 실패했습니다.");	
		return "redirect:/user/member/join"; 
	}
	
	// 아이디 중복검사
	@ResponseBody
	@GetMapping("/checkLoginId")
	public Map<String, Boolean> checkLoginId(@RequestParam String loginId) {

        UserDto dto = service.findUser(
                Map.of("loginId", loginId));

        return Map.of("exists", dto != null);
    }
	
	// 닉네임 중복검사
	@ResponseBody
	@GetMapping("/checkNickname")
	public Map<String, Boolean> checkNickname(@RequestParam String nickname) {

        UserDto dto = service.findUser(
                Map.of("nickname", nickname));

        return Map.of("exists", dto != null);
    }
	
	// 로그인
	@GetMapping("/login")
	public String login() {  return "user/member/login"; }

	@GetMapping("/fail") public String fail(Model model) {
		model.addAttribute("errorMessage","로그인 실패 : 아이디 또는 비밀번호를 확인해주세요.");
		return "redirect:/user/member/join";
	}	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage") public String  mypage( Authentication   authentication , Model model  ) {  
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			
		} 
		System.out.println(".........."+user);
		System.out.println(".........."+loginId);
		model.addAttribute("dto" , user); 
		return "user/member/mypage"; 
	}
	
	// 아이디 찾기
	@GetMapping("/findId")
	public String findId() {
		return "user/member/findId";
	}
	
	
	
}
