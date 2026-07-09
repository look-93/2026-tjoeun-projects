package com.moit.meetup.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.service.AdvertisementService;
import com.moit.meetup.dto.MeetupApplicationDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupLikeDto;
import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.service.MeetupService;
import com.moit.review.dto.ReviewDto;
import com.moit.review.service.ReviewService;
import com.moit.util.UtilPaging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MeetupController {
	@Autowired MeetupService meetupService;
	@Autowired AdvertisementService advertisementService;
	@Autowired ReviewService reviewService;
	
	/* 메인페이지 광고 */
	@GetMapping("/main")
    public String main(Model model,
		            HttpServletRequest request,
		            HttpSession session) {

		Integer memberId =
				 (Integer)session.getAttribute("loginMemberId");
		
		String sessionId =
		        session.getId();
		
        AdvertisementDto mainAd =
                advertisementService.selectTopAdvertisement("MAIN", memberId, sessionId);
        //System.out.println(mainAd + "dddddddddddddddddddddddddddddddddddddddddddd");
        // 광고가 존재하면 노출 증가
        if(mainAd != null) {

            boolean counted =
                advertisementService.insertImpressionLog(
                    mainAd.getAdId(),
                    request,
                    session
                );

            if(counted){
                advertisementService.updateImpressions(mainAd.getAdId());
            }
        }
        
        model.addAttribute("mainAd", mainAd);
        

        return "user/main";
    }
	
	/*1. 사용자 - 모임 리스트 화면(HTML) 호출*/
	@GetMapping("/meetup/list")
	public String listPage(Model model,
            HttpServletRequest request,
            HttpSession session) {

		Integer memberId =
				 (Integer)session.getAttribute("loginMemberId");
		
		String sessionId =
		        session.getId();
		
	    AdvertisementDto banner = advertisementService.selectTopAdvertisement( "MEETUP_LIST_BANNER" , memberId, sessionId);  
	    AdvertisementDto sidebar = advertisementService.selectTopAdvertisement( "MEETUP_LIST_SIDEBAR" , memberId, sessionId);

	    model.addAttribute("bannerAd", banner);
	    model.addAttribute("sidebarAd", sidebar);
	    // 광고가 존재하면 노출 증가
	    if(banner != null){

	        boolean counted =
	            advertisementService.insertImpressionLog(
	                banner.getAdId(),
	                request,
	                session
	            );

	        if(counted){
	            advertisementService.updateImpressions(
	                banner.getAdId()
	            );
	        }
	    }
        if(sidebar != null) {

            boolean counted =
                advertisementService.insertImpressionLog(
                    banner.getAdId(),
                    request,
                    session
                );

            if(counted){
                advertisementService.updateImpressions(
                    banner.getAdId()
                );
            }
        }
	    
		return "/user/meetup/list";
	}
	
	/*2. 사용자 - 모임 리스트 데이터(JSON) 호출*/
	@GetMapping("/meetup/list/data")
	@ResponseBody
	public Map<String, Object> listData(MeetupSearchDto meetupSearchDto, Model model){
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
	public String detail(Model model, Authentication authentication, 
						MeetupApplicationDto meetupApplicationDto, 
						@RequestParam(value = "sort", required = false, defaultValue = "latest")  String sort,
			            HttpServletRequest request, HttpSession session	) {
		
		Integer memberId =
				 (Integer)session.getAttribute("loginMemberId");
		
		String sessionId =
		        session.getId();
		
//		CustomUser user = (CustomUser) authentication.getPrincipal(); 
//		int memberId = userMeetupService.findByMamberId(user.getUsername());
//		meetupApplicationsDto.setMemberId(memberId);
		
		AdvertisementDto desidebar = advertisementService.selectTopAdvertisement( "MEETUP_DETAIL_SIDEBAR" , memberId, sessionId);

		meetupApplicationDto.setMemberId(2);
		model.addAttribute("desidebarAd", desidebar);
		// 광고가 존재하면 노출 증가
		if(desidebar != null){

		    boolean counted =
		        advertisementService.insertImpressionLog(
		            desidebar.getAdId(),
		            request,
		            session
		        );

		    if(counted){
		        advertisementService.updateImpressions(
		            desidebar.getAdId()
		        );
		    }
		}
		
		meetupApplicationDto.setStatusList(Arrays.asList("PENDING", "APPROVED"));
		model.addAttribute("applyInfo",meetupService.findApplyInfo(meetupApplicationDto));
		model.addAttribute("detail", meetupService.selectMeetupDetail(meetupApplicationDto.getMeetupId()));
		//System.out.println(meetupService.selectMeetupDetail(meetupApplicationDto.getMeetupId()));
		List<ReviewDto> reviewList = reviewService.selectUserReview(meetupApplicationDto.getMeetupId(), sort);
		model.addAttribute("reviews", reviewList);
		
		return "user/meetup/detail";
	}
	
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
	
	
	//마이페이지 - 내 모집글 정보
	@GetMapping("/mypage/myMeetupInfo")
	public String myMeetupList(Model model, MeetupDto meetupdto, Authentication authentication, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
		// 멤버완료 취합 후 적용
//		CustomUser user = (CustomUser) authentication.getPrincipal();		
//		int memberId = userMeetupService.findByMamberId(user.getUsername());		
//		meetupdto.setMemberId(memberId);
		
		meetupdto.setMemberId(2);
		//SystSystem.out.println(meetupdto.getMeetupId());em.out.println(meetupService.selectMyMeetup(pstartno,meetupdto));
		System.out.println(meetupdto.getMeetupId());
		model.addAttribute("meetupStats", meetupService.selectMyPageStats(meetupdto.getMemberId())); //통계
		model.addAttribute("meetupList", meetupService.selectMyMeetup(pstartno,meetupdto));
		model.addAttribute("paging", new UtilPaging(meetupService.selectMyMeetupTotalCnt(meetupdto), pstartno));
		
		//model.addAttribute("meetupApplyMemberList", meetupService.selectMeetupApplyMember(meetupdto.getMeetupId())); //신청자목록
		
		
		return "user/mypage/meetup/meetupInfo";
	}		
	
	
	//마이페이지 - 내 모집글 조회
	@GetMapping("/mypage/meetupMember")
	@ResponseBody
	public Map<String, Object> myMeetupMemberList(Model model, int meetupId) {
		
		Map<String, Object> result = new HashMap<>();
		List<MeetupDto> list= meetupService.selectMeetupApplyMember(meetupId);
		result.put("list", list);
	
		return result;
	}	
	
	//마이페이지 - 내 모집글 조회
	@GetMapping("/mypage/updateApplyStatus")
	@ResponseBody
	public Map<String, Object> myMeetupApplyStatus(Model model, MeetupApplicationDto meetupApplicationDto) {
		
		Map<String, Object> result = new HashMap<>();
		boolean insert = meetupService.changeMeetupApplyStatus(meetupApplicationDto) > 0;	
		result.put("insert", insert);
		return result;
	}		
	
	//모집글 수정 조회	
	@GetMapping("/mypage/update")
	public String update(Model model, int meetupId) {
		//System.out.println(meetupId + "ddddddddddddddddddddddd");
		
		model.addAttribute("meetup", meetupService.selectMeetupDetail(meetupId));
		model.addAttribute("childCategoryList", meetupService.findAllChildCategory());		
		model.addAttribute("sigunguList", meetupService.findAllSigungu());
		return "user/meetup/write";
	}	
	
	//모집글 수정
	@PostMapping("/mypage/update")
	public String updateMeetup(Model model, MeetupDto meetupdto, RedirectAttributes rttr, Authentication authentication) {
		// 멤버완료 취합 후 적용
//		CustomUser user = (CustomUser) authentication.getPrincipal();		
//		int memberId = userMeetupService.findByMamberId(user.getUsername());		
//		meetupdto.setMemberId(memberId);
		
		meetupdto.setMemberId(2);
		boolean result = meetupService.updateMeetup(meetupdto) > 0;		
		rttr.addFlashAttribute("result", result);		
		return "redirect:/user/mypage/meetup/meetupInfo";
	}		
	
	
	
	//마이페이지 내 신청글 조회
	@GetMapping("/mypage/meetupApplyInfo")
		public String myMeetupApplyList(Model model, MeetupDto meetupdto, Authentication authentication, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
			// 멤버완료 취합 후 적용
//			CustomUser user = (CustomUser) authentication.getPrincipal();		
//			int memberId = userMeetupService.findByMamberId(user.getUsername());		
//			meetupdto.setMemberId(memberId);
			
			meetupdto.setMemberId(2);		
			model.addAttribute("meetupStats", meetupService.selectMyPageStats(meetupdto.getMemberId()));
			model.addAttribute("applyList", meetupService.selectMyMeetupApply(pstartno,meetupdto));
			model.addAttribute("paging", new UtilPaging(meetupService.selectMyMeetupApplyTotalCnt(meetupdto), pstartno));
			//model.addAttribute("menu", "meetupApply");
			
			return "user/mypage/meetup/meetupApplicationInfo";
		}	
	
	//모임 - 작성
	@GetMapping("/meetup/write")
	public String write() {
		return "user/meetup/write";
	}	
}
