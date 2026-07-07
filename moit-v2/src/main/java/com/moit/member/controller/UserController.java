package com.moit.member.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.member.dto.UserDto;
import com.moit.member.service.UserService;


@Controller
public class UserController {

	@Autowired  UserService service;
	
	@RequestMapping( "/" )
	public String index() {  return "user/main"; }

	 
	///////////////////////////////////////
	@RequestMapping( value="/user/join" , method=RequestMethod.GET  )
	public String join() {  return "user/join"; }
	
	@RequestMapping( value="/user/join" , method=RequestMethod.POST  )
	public String join_post(UserDto dto, RedirectAttributes rttr) {  
		
		int result = service.insert(dto);
		
		if(result==1) {
			rttr.addFlashAttribute("msg", "회원가입이 완료되었습니다.");
			return "redirect:/user/login"; 
		}
		else if(result==0) {
			rttr.addFlashAttribute("msg", "이미 사용중인 아이디입니다.");
			return "redirect:/user/join"; 
		}
		else if(result==-1){
			rttr.addFlashAttribute("msg", "이미 사용중인 닉네임입니다.");
			return "redirect:/user/join"; 
		}
		
		rttr.addFlashAttribute("msg", "회원가입에 실패했습니다.");	
		return "redirect:/user/join"; 
	}
	// 아이디 중복검사
	@ResponseBody
	@RequestMapping(value="/users/checkLoginId",method=RequestMethod.GET)
	public Map<String,Object> checkLoginId(@RequestParam String loginId){
		
		
		Map<String,Object> map = new HashMap<>();
		map.put("loginId", loginId);
		
		UserDto dto = service.findMember(map);
		
		Map<String,Object> result = new HashMap<>();
		result.put("exists", dto != null);
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/user/checkNickname", method=RequestMethod.GET)
	public Map<String,Object> checkNickname(@RequestParam String nickname){

	    Map<String,Object> map=new HashMap<>();
	    map.put("nickname",nickname);

	    UserDto dto=service.findMember(map);

	    Map<String,Object> result=new HashMap<>();
	    result.put("exists", dto!=null);

	    return result;
	}
	
	@RequestMapping( value="/user/login" , method=RequestMethod.GET  )
	public String login() {  return "user/login"; }

	/*
	 * @RequestMapping(value="/users/mypage", method = RequestMethod.GET) public
	 * String mypage(Model model, Principal principal) {
	 * model.addAttribute("dto",service.findByLoginId(principal.getName())); return
	 * "users/mypage"; }
	 */		
}
