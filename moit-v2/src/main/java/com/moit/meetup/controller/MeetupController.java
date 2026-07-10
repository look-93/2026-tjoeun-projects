package com.moit.meetup.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.service.AdvertisementService;
import com.moit.meetup.dto.MeetupApplicationDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupLikeDto;
import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.service.MeetupService;
import com.moit.member.dto.UserDto;
import com.moit.review.dto.ReviewDto;
import com.moit.review.service.ReviewService;
import com.moit.security.CustomUserDetails;
import com.moit.util.UtilPaging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MeetupController {
	@Autowired MeetupService meetupService;
	@Autowired AdvertisementService advertisementService;
	@Autowired ReviewService reviewService;
	

	
	/*1. 모임 리스트 화면(HTML) 호출*/
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
                    "MEETUP_LIST_BANNER",
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
                	sidebar.getAdId(),
                    "MEETUP_LIST_SIDEBAR",
                    request,
                    session
                );

            if(counted){
                advertisementService.updateImpressions(
                	sidebar.getAdId()
                );
            }
        }
	    
		return "/user/meetup/list";
	}
	
	/*2. 모임 리스트 데이터(JSON) 호출*/
	@GetMapping("/meetup/list/data")
	@ResponseBody
	public Map<String, Object> listData(MeetupSearchDto meetupSearchDto, Model model){
		Integer pstartno = meetupSearchDto.getPstartno();
		
		if(pstartno == null || pstartno <= 0) {
			pstartno = 1;
			meetupSearchDto.setPstartno(1);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("paging", new UtilPaging(meetupService.findAllMeetupCountBy(meetupSearchDto), pstartno));
		map.put("searchList", meetupService.findAllMeetupBy(pstartno, meetupSearchDto));
		map.put("sidoList", meetupService.findAllSido());
		map.put("categorys", meetupService.findAllCategory());
		//System.out.println(meetupSearchDto.getSearchText());
		
		return map;
	}
	
	/* 좋아요 기능*/
	@PostMapping("/meetup/list/like")
	@ResponseBody
	public Map<String, Object> meetupLike(MeetupLikeDto meetupLikeDto, Authentication authentication, MeetupDto meetupdto) {
		
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		meetupLikeDto.setMemberId(memberId);
		Map<String, Object> result = new HashMap<>();
		
		//meetupLikeDto.setMemberId(3);
		boolean hasLike = meetupService.insertMeetupLike(meetupLikeDto);	
		result.put("hasLike", hasLike);
	    result.put("likeCnt", meetupService.countMeetupLike(meetupLikeDto));
		return result;	
	}	
	

	/* 사용자 - 모임상세조회 */
	@GetMapping("/meetup/detail")
	public String detail(Model model, Authentication authentication, 
						MeetupApplicationDto meetupApplicationDto, 
						@RequestParam(value = "keyword", required = false) String keyword,   // ★ 추가
						@RequestParam(value = "sort", required = false, defaultValue = "latest")  String sort,
			            HttpServletRequest request, HttpSession session	) {
		
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		
		String sessionId =
		        session.getId();
		
//		CustomUser user = (CustomUser) authentication.getPrincipal(); 
//		int memberId = userMeetupService.findByMamberId(user.getUsername());
//		meetupApplicationsDto.setMemberId(memberId);
	    AdvertisementDto desidebar =
	            advertisementService.selectTopAdvertisement("MEETUP_DETAIL_SIDEBAR", memberId, sessionId);

	    meetupApplicationDto.setMemberId(memberId);
	    model.addAttribute("desidebarAd", desidebar);

	    // 광고가 존재하면 노출 증가
	    if(desidebar != null){

	          boolean counted =
	              advertisementService.insertImpressionLog(
	                  desidebar.getAdId(),
	                    "MEETUP_DETAIL_SIDEBAR",
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
	    model.addAttribute("applyInfo", meetupService.findApplyInfo(meetupApplicationDto));
	    model.addAttribute("detail", meetupService.selectMeetupDetail(meetupApplicationDto.getMeetupId()));
	    model.addAttribute("images", meetupService.findMeetupImage(meetupApplicationDto.getMeetupId()));

	    //로그인한 사용자 html 로 전달(후기)
	    model.addAttribute("loginMemberId", memberId);

	    //  기존 부분
	    // List<ReviewDto> reviewList =
	    //        reviewService.selectUserReview(meetupApplicationDto.getMeetupId(), sort);
	    // model.addAttribute("reviews", reviewList);

	    // ★ 추가 시작
	    List<ReviewDto> reviewList;

	    if (keyword != null && !keyword.isBlank()) {
	        reviewList = reviewService.selectReviewByContent(
	                meetupApplicationDto.getMeetupId(),
	                keyword,
	                sort);
	    } else {
	        reviewList = reviewService.selectUserReview(
	                meetupApplicationDto.getMeetupId(),
	                sort);
	    }

	    model.addAttribute("reviews", reviewList);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("sort", sort);
	    // ★ 추가 끝
	    
	    return "user/meetup/detail";
	}
	
	//모집신청
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/meetup/applyMeetup")
	@ResponseBody
	public Map<String, Object> applyMeetup(@RequestBody MeetupApplicationDto  meetupApplicationDto, MeetupDto meetupdto, Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
		
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 		

		meetupApplicationDto.setMemberId(memberId);

		boolean result = meetupService.insertApplication(meetupApplicationDto ) > 0;
		map.put("result", result);
		return map;
	}
		

	//모집신청취소
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/meetup/cancelApplyMeetup")
	@ResponseBody
	public Map<String, Object> cancelApplyMeetup(@RequestBody MeetupApplicationDto  meetupApplicationDto, MeetupDto meetupdto, Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
		
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		meetupApplicationDto.setMemberId(memberId);

		boolean result = meetupService.cancelApplyMeetup(meetupApplicationDto ) > 0;
		map.put("result", result);
		return map;
	}	
	

	
	//마이페이지 - 내 모집글 정보
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage/myMeetupInfo")
	public String myMeetupList(Model model, MeetupDto meetupdto, Authentication authentication, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
		// 멤버완료 취합 후 적용
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		
		model.addAttribute("dto" , user); 
		meetupdto.setMemberId(memberId);
		//SystSystem.out.println(meetupdto.getMeetupId());em.out.println(meetupService.selectMyMeetup(pstartno,meetupdto));
		//System.out.println(meetupdto.getMeetupId());
		model.addAttribute("meetupStats", meetupService.selectMyPageStats(memberId)); //통계
		model.addAttribute("meetupList", meetupService.selectMyMeetup(pstartno,meetupdto));
		model.addAttribute("paging", new UtilPaging(meetupService.selectMyMeetupTotalCnt(meetupdto), pstartno));
		
		//model.addAttribute("meetupApplyMemberList", meetupService.selectMeetupApplyMember(meetupdto.getMeetupId())); //신청자목록
		
		
		return "user/mypage/meetup/meetupInfo";
	}		
	
	
	//마이페이지 - 내 모집글 조회
	@GetMapping("/mypage/meetupMember")
	@ResponseBody
	public Map<String, Object> myMeetupMemberList(int meetupId) {
		
		Map<String, Object> result = new HashMap<>();
		List<MeetupDto> list= meetupService.selectMeetupApplyMember(meetupId);
		result.put("list", list);
	
		return result;
	}	
	
	//마이페이지 - 내 모집글 조회 - 신청자 목록 업데이트
	@GetMapping("/mypage/updateApplyStatus")
	@ResponseBody
	public Map<String, Object> myMeetupApplyStatus(MeetupApplicationDto meetupApplicationDto) {
		
		Map<String, Object> result = new HashMap<>();
		boolean insert = meetupService.changeMeetupApplyStatus(meetupApplicationDto) > 0;
		//System.out.println(meetupApplicationDto.getApplicationId() + "fgsgsdsggggggggggggggggggggggg");
		result.put("insert", insert);
		return result;
	}			
	
	//모임 - 작성
	@GetMapping("/meetup/write")
	public String write(Model model) {
		model.addAttribute("childCategoryList", meetupService.findAllChildCategory());
		model.addAttribute("sigunguList", meetupService.findAllSigungu());
		return "user/meetup/write";
	}
	
	@PostMapping("/meetup/write")
	public String createMeetup(Model model, 
							   MeetupDto meetupdto, 
							   RedirectAttributes rttr, 
							   Authentication authentication, 
							   @RequestParam(value = "files", required = false) List<MultipartFile> files) {
		// 멤버완료 취합 후 적용
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		
		meetupdto.setMemberId(memberId);
		//System.out.println(meetupdto.getMeetupId());
		
		//System.out.println(files + "ddddddddddddddddddddddddddddddddddddddd");
		
		boolean result = meetupService.insertMeetup(meetupdto, files) > 0;		
		rttr.addFlashAttribute("result", result);
		
		return "redirect:/meetup/detail?meetupId=" + meetupdto.getMeetupId();
	}	
	
	//모집글 수정 조회	
	@GetMapping("/mypage/update")
	public String update(Model model, int meetupId) {
		//System.out.println(meetupId + "ddddddddddddddddddddddd");
		
		model.addAttribute("images", meetupService.findMeetupImage(meetupId));
		model.addAttribute("meetup", meetupService.selectMeetupDetail(meetupId));
		model.addAttribute("childCategoryList", meetupService.findAllChildCategory());		
		model.addAttribute("sigunguList", meetupService.findAllSigungu());
		return "user/meetup/write";
	}	
	
	//모집글 수정
	@PostMapping("/mypage/update")
	public String updateMeetup(Model model, MeetupDto meetupdto, RedirectAttributes rttr, Authentication authentication, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
		// 멤버완료 취합 후 적용
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 
		
		meetupdto.setMemberId(memberId);
		boolean result = meetupService.updateMeetup(meetupdto, files) > 0;		
		rttr.addFlashAttribute("result", result);		
		return "redirect:/mypage/myMeetupInfo";
	}		

	//마이페이지 내 신청글 조회
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage/meetupApplyInfo")
		public String myMeetupApplyList(Model model, MeetupDto meetupdto, Authentication authentication, @RequestParam(value="pstartno", defaultValue="1") int pstartno) {
			// 멤버완료 취합 후 적용
			String loginId     = null, provider = null;
			UserDto user=null;
			Object principal = authentication.getPrincipal();
			Integer memberId = null;
			//1. local
			if(   principal   instanceof CustomUserDetails ) {
				CustomUserDetails  users = (CustomUserDetails)principal;
				user=users.getUser();
				loginId    =  users.getUser().getLoginId();
				memberId = users.getUser().getMemberId();
			} 
			
			meetupdto.setMemberId(memberId);		
			model.addAttribute("dto" , user); 
			model.addAttribute("meetupStats", meetupService.selectMyPageStats(memberId));
			model.addAttribute("applyList", meetupService.selectMyMeetupApply(pstartno,meetupdto));
			model.addAttribute("paging", new UtilPaging(meetupService.selectMyMeetupApplyTotalCnt(meetupdto), pstartno));
			//model.addAttribute("menu", "meetupApply");
			
			return "user/mypage/meetup/meetupApplicationInfo";
		}
	
	//마이페이지 - 모집글 삭제
	@PostMapping("/mypage/meetup/delete")
	public String deleteByAdmin(int meetupId, @RequestParam(value = "pstartno", required = false, defaultValue = "1") Integer pstartno, RedirectAttributes rttr) {
		meetupService.updateMeetupDeleteYn(meetupId);		
		return "redirect:/mypage/myMeetupInfo?pstartno=" + pstartno;
	}

}
