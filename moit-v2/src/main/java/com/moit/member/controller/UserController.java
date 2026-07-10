package com.moit.member.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.member.dto.UserDto;
import com.moit.member.service.UserService;
import com.moit.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user/member")
public class UserController {

	@Autowired UserService service;
	
//	// 메인페이지
//	@GetMapping("/")
//	public String index() {  return "/main"; }
	
	// 회원가입
	@PreAuthorize("isAnonymous()")
	@GetMapping("/join")
	public String join() {  return "user/member/join"; }
	
	@PreAuthorize("isAnonymous()")
	@PostMapping("/join")
	public String join_post(UserDto dto, HttpServletRequest request, RedirectAttributes rttr) {  
		
		dto.setJoinIp(getClientIp(request));
		
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
	@PreAuthorize("isAnonymous()")
	@GetMapping("/login")
	public String login() {  return "user/member/login"; }

	@GetMapping("/fail") public String fail(Model model) {
		model.addAttribute("errorMessage","로그인 실패 : 아이디 또는 비밀번호를 확인해주세요.");
		return "redirect:/user/member/join";
	}
	
	// 소셜 로그인
	@GetMapping("/social-info")
    public String socialInfoForm() {
        return "user/member/social-info";
    }

    @PostMapping("/social-info")
    public String socialInfoSave(
            @ModelAttribute UserDto dto,
            Authentication authentication) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        dto.setMemberId(user.getAppUserId());

        service.completeSocialJoin(dto);

        return "redirect:/user/member/mypage";
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
	@PreAuthorize("isAnonymous()")
	@GetMapping("/findId") public String findIdPage() { return "user/member/findId"; }
	
	@PreAuthorize("isAnonymous()")
	@PostMapping("/findId")
	public String findId(UserDto dto , Model model) {
		UserDto user = service.findId(dto);
		
		model.addAttribute("user",user);
		
		return "user/member/findIdResult";
	}
	
	// 비밀번호 찾기
	@PreAuthorize("isAnonymous()")
	@GetMapping("/findPassword") public String findPasswordPage() { return "user/member/findPassword"; }
	
	@PreAuthorize("isAnonymous()")
	@PostMapping("/findPassword")
	public String findPassword(UserDto dto, Model model,HttpSession session) {
		
		UserDto user = service.findPasswordUser(dto);
		
		if(user == null) { 
			model.addAttribute("error","일치하는 회원이 없습니다.");
			return "user/member/findPassword";
		}
		
		session.setAttribute("findMemberId", user.getMemberId());
		
		return "user/member/changePassword";
	}
	
	// 비밀번호 재발급
	@PreAuthorize("isAnonymous()")
	@PostMapping("/changePassword")
	public String changePassword(UserDto dto, HttpSession session,
	                             RedirectAttributes rttr) {
		Integer memberId = (Integer)session.getAttribute("findMemberId");
		
		if(memberId == null) {
			return "redirect:/user/member/findPassword";
		}
		
		dto.setMemberId(memberId);
		
	    service.changePassword(dto);
	    
	    session.removeAttribute("findMemberId");

	    rttr.addFlashAttribute("message", "비밀번호가 변경되었습니다.");

	    return "redirect:/user/member/login";
	}
	
	// 회원가입 시 IP 조회
	private String getClientIp(HttpServletRequest request) {

		 String ip = request.getHeader("X-Forwarded-For");
	
		    if (ip == null || ip.isEmpty()) {
		        ip = request.getRemoteAddr();
		    }	
		    return ip;
	}
	
	// 회원정보 수정
	@GetMapping("/memberEdit")
	public String memberEdit(Authentication authentication ,Model model) {
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		
		UserDto dto = new UserDto();
		dto.setLoginId(user.getUsername());
		
		dto = service.findByLoginId(dto);
		
		model.addAttribute("dto",dto);
		
		return "user/member/memberEdit";		
	}
	
	@PostMapping("memberEdit")
	public String memberEdit(UserDto dto) {
		service.updateUser(dto);
		
		return "redirect:/user/member/mypage";
	}
	
}
