package com.moit.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.dto.MeetupApplicationsDto;
import com.moit.dto.MeetupDto;
import com.moit.dto.MeetupLikeDto;
import com.moit.dto.MeetupSearchDto;
import com.moit.security.CustomUser;
import com.moit.service.QuestionService;
import com.moit.service.UserMeetupService;
import com.moit.util.PagingUtil;

@Controller
public class UserMeetupController {
	@Autowired UserMeetupService userMeetupService;
	@Autowired QuestionService questionService;
	@RequestMapping("/meetup/user/list.do")
	public String serchByUser(Model model, MeetupSearchDto meetupSerchDto,Authentication authentication, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
		CustomUser user = (CustomUser) authentication.getPrincipal();
		int memberId = userMeetupService.findByMamberId(user.getUsername());
		meetupSerchDto.setMemberId(memberId);
		
		model.addAttribute("menu", "meetup");
		model.addAttribute("sidoList", userMeetupService.findAllSido());
		model.addAttribute("categoryList", userMeetupService.findAllCategory());
		model.addAttribute("paging", new PagingUtil(userMeetupService.selectUserMeetupTotalCnt(meetupSerchDto), pstartno));
		model.addAttribute("serchList", userMeetupService.searchByUser(pstartno,meetupSerchDto));	
		return "meetup/user/list";
	}
	
	@RequestMapping("/meetup/user/detail.do")
	public String detail(Model model,  MeetupDto meetupdto, Authentication authentication, MeetupApplicationsDto meetupApplicationsDto) {
		CustomUser user = (CustomUser) authentication.getPrincipal();
		int memberId = userMeetupService.findByMamberId(user.getUsername());
		meetupApplicationsDto.setMemberId(memberId);
		meetupApplicationsDto.setStatusList(Arrays.asList("PENDING", "APPROVED"));
		model.addAttribute("applyInfo", userMeetupService.findApplyInfo(meetupApplicationsDto));
		model.addAttribute("detail", userMeetupService.selectMeetupDetail(meetupApplicationsDto.getMeetupId()));
		model.addAttribute( "qnaList", questionService.selectByParentId(meetupApplicationsDto.getMeetupId()) );
		return "meetup/user/detail";
	}
	
	@RequestMapping(value="/meetup/user/write.do", method = RequestMethod.GET)
	public String write(Model model) {
		model.addAttribute("childCategoryList", userMeetupService.findAllChildCategory());
		model.addAttribute("sigunguList", userMeetupService.findAllSigungu());
		return "meetup/user/write";
	}
	
	@RequestMapping(value="/meetup/user/write.do", method = RequestMethod.POST)
	public String createMeetup(Model model, MeetupDto meetupdto, RedirectAttributes rttr, Authentication authentication) {
		// 멤버완료 취합 후 적용
		CustomUser user = (CustomUser) authentication.getPrincipal();		
		int memberId = userMeetupService.findByMamberId(user.getUsername());		
		meetupdto.setMemberId(memberId);
		
		//meetupdto.setMemberId();
		boolean result = userMeetupService.insertMeetup(meetupdto) > 0;		
		rttr.addFlashAttribute("result", result);		
		return "redirect:/meetup/user/detail.do?meetupId=" + meetupdto.getMeetupId();
	}
	
	
	///////////////////////////////마이페이지///////////////////////////////////		
	@RequestMapping(value="/meetup/mypage/update.do", method = RequestMethod.GET)
	public String update(Model model, int meetupId) {
		model.addAttribute("meetup", userMeetupService.selectMeetupDetail(meetupId));
		model.addAttribute("childCategoryList", userMeetupService.findAllChildCategory());		
		model.addAttribute("sigunguList", userMeetupService.findAllSigungu());
		return "meetup/user/write";
	}	
	
	@RequestMapping(value="/meetup/mypage/update.do", method = RequestMethod.POST)
	public String updateMeetup(Model model, MeetupDto meetupdto, RedirectAttributes rttr, Authentication authentication) {
		// 멤버완료 취합 후 적용
		CustomUser user = (CustomUser) authentication.getPrincipal();		
		int memberId = userMeetupService.findByMamberId(user.getUsername());		
		meetupdto.setMemberId(memberId);
		
		//meetupdto.setMemberId(1);
		boolean result = userMeetupService.updateMeetup(meetupdto) > 0;		
		rttr.addFlashAttribute("result", result);		
		return "redirect:/meetup/mypage/meetup.do";
	}	
	
	@RequestMapping(value = "/meetup/mypage/delete.do",  method = RequestMethod.POST)
	public String deleteByAdmin(int meetupId) {
	
		userMeetupService.deleteMeetup(meetupId);
		return "redirect:/meetup/mypage/meetup.do";
	}
	
	//마이페이지 - 내 모집글 조회
	@RequestMapping("/meetup/mypage/meetup.do")
	public String myMeetupList(Model model, MeetupDto meetupdto, RedirectAttributes rttr, Authentication authentication, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
		// 멤버완료 취합 후 적용
		CustomUser user = (CustomUser) authentication.getPrincipal();		
		int memberId = userMeetupService.findByMamberId(user.getUsername());		
		meetupdto.setMemberId(memberId);
		
		//meetupdto.setMemberId(1);
		//System.out.println(meetupdto.getMeetupId());
		model.addAttribute("meetupApplyMemberList", userMeetupService.selectMeetupApplyMember(meetupdto.getMeetupId())); //신청자목록
		model.addAttribute("meetupStats", userMeetupService.selectMyPageStats(memberId)); //통계
		model.addAttribute("meetupList", userMeetupService.selectMyMeetup(pstartno,meetupdto));
		model.addAttribute("paging", new PagingUtil(userMeetupService.selectMyMeetupTotalCnt(meetupdto), pstartno));
		model.addAttribute("menu", "meetup");
		
		return "meetup/user/myMeetupList";
	}	
	
	//마이페이지 - 내 모집글 조회
	@RequestMapping("/meetup/mypage/meetupMember.do")
	@ResponseBody
	public Map<String, Object> myMeetupMemberList(Model model, int meetupId) {
		
		Map<String, Object> result = new HashMap<>();
		List<MeetupDto> list= userMeetupService.selectMeetupApplyMember(meetupId);
		result.put("list", list);
	
		return result;
	}	
	
	//마이페이지 - 내 모집글 조회
	@RequestMapping("/meetup/mypage/updateApplyStatus.do")
	@ResponseBody
	public Map<String, Object> myMeetupApplyStatus(Model model, MeetupApplicationsDto meetupApplicationsDto) {
		
		Map<String, Object> result = new HashMap<>();
		boolean insert = userMeetupService.changeMeetupApplyStatus(meetupApplicationsDto) > 0;	
		result.put("insert", insert);
		return result;
	}	
	
	////////////////모집신청////////////////////
	//모집신청
	@RequestMapping(value="/meetup/user/applyMeetup.do", method = RequestMethod.POST)
	public String applyMeetup(Model model ,MeetupApplicationsDto  meetupApplicationsDto , RedirectAttributes rttr, MeetupDto meetupdto, Authentication authentication) {
		
		CustomUser user = (CustomUser) authentication.getPrincipal();		
		int memberId = userMeetupService.findByMamberId(user.getUsername());		
		meetupApplicationsDto.setMemberId(memberId);
		
		//meetupApplicationsDto.setStatusList(Arrays.asList("CANCELED"));
		boolean result = userMeetupService.insertApplication(meetupApplicationsDto ) > 0;
		rttr.addFlashAttribute("result", result);		
		return "redirect:/meetup/user/detail.do?meetupId=" + meetupApplicationsDto.getMeetupId();
	}
	
	//모집신청취소
	@RequestMapping(value="/meetup/user/cancelApplyMeetup.do", method = RequestMethod.POST)
	public String cancelApplyMeetup(Model model ,MeetupApplicationsDto  meetupApplicationsDto , RedirectAttributes rttr) {
		boolean result = userMeetupService.cancelApplyMeetup(meetupApplicationsDto ) > 0;
	
		rttr.addFlashAttribute("result", result);		
		return "redirect:/meetup/user/detail.do?meetupId=" + meetupApplicationsDto.getMeetupId();
	}
	
	//마이페이지 내 신청글 조회
	@RequestMapping("/meetup/mypage/apply.do")
	public String myMeetupApplyList(Model model, MeetupDto meetupdto, RedirectAttributes rttr, Authentication authentication, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
		// 멤버완료 취합 후 적용
		CustomUser user = (CustomUser) authentication.getPrincipal();		
		int memberId = userMeetupService.findByMamberId(user.getUsername());		
		meetupdto.setMemberId(memberId);
		
		//meetupdto.setMemberId(3);		
		model.addAttribute("meetupStats", userMeetupService.selectMyPageStats(memberId));
		model.addAttribute("applyList", userMeetupService.selectMyMeetupApply(pstartno,meetupdto));
		model.addAttribute("paging", new PagingUtil(userMeetupService.selectMyMeetupApplyTotalCnt(meetupdto), pstartno));
		model.addAttribute("menu", "meetupApply");
		
		return "meetup/user/myMeetupApplicationList";
	}
	
	//좋아요기능처리
	@RequestMapping(value= "/meetup/user/meetupLike.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> meetupLike(MeetupLikeDto meetupLikeDto, Authentication authentication, MeetupDto meetupdto) {
		CustomUser user = (CustomUser) authentication.getPrincipal();		
		int memberId = userMeetupService.findByMamberId(user.getUsername());		
		meetupLikeDto.setMemberId(memberId);
		Map<String, Object> result = new HashMap<>();
		
		//meetupLikeDto.setMemberId(3);
		boolean hasLike = userMeetupService.insertMeetupLike(meetupLikeDto);	
		result.put("hasLike", hasLike);
	    result.put("likeCnt",
	            userMeetupService.countMeetupLike(meetupLikeDto.getMeetupId()));
		return result;
	
	}		
}
