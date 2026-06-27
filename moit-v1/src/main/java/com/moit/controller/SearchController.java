package com.moit.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moit.dto.UserDto;
import com.moit.service.UserService;



@Controller
public class SearchController {
	@Autowired UserService service;
	
	@ResponseBody
	@RequestMapping(value="/duplicateNickname", method=RequestMethod.GET)
	public Map<String, Object> duplicateNickname( @RequestParam("nickname") String nickname){
				Map<String, Object> result = new HashMap<>();
//		
//		//db에서 이메일 존재여부 확인
//		UserDto find = service.findByNickname(nickname);
//		if(find != null) {
//			result.put("exists", true);
//		}else {
//			result.put("exists", false);
//		}
		return result;
	}	
	
	
	@ResponseBody
	@RequestMapping(value="/duplicateLoginId", method=RequestMethod.GET)
	public Map<String, Object> duplicateEmail( @RequestParam("loginId") String loginId){
		
		Map<String, Object> result = new HashMap<>();
		
		//db에서 이메일 존재여부 확인
//		UserDto find = service.findByLoginId(loginId);
//		if(find != null) {
//			result.put("exists", true);
//		}else {
//			result.put("exists", false);
//		}
		return result;
	}		

}
