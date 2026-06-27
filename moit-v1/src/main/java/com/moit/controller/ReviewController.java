package com.moit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.dto.ReviewDto;
import com.moit.service.ReviewService;

@Controller
public class ReviewController {
	@Autowired ReviewService reviewservice;
	
	// 후기작성 처리 후 (마이페이지 또는 일반 모달에서 후기작성 처리)
	@RequestMapping(value="/review/test", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertUserReview_post(ReviewDto dto) {
		Map<String, Object> response = new HashMap<>();
		response.put( "success" ,   reviewservice.insertUserReview(dto) == 1 );
		return response;
	}  // http://localhost:8080/moit/meetup/user/detail.do?meetupId=22
	
	//내 해당모임의 후기
	// 특정모임의 후기목록 조회
	@RequestMapping(value="/review/testList", method=RequestMethod.GET)
	@ResponseBody
	public  List<ReviewDto> testList  (@RequestParam("meetupId") int meetupId) {
		return  reviewservice.selectUserReview(meetupId);
	}
	
	
	
	
//	@RequestMapping(value="/review/test", method=RequestMethod.POST)
//	public String insertUserReview_post(ReviewDto dto, RedirectAttributes rttr) {
//		reviewservice.insertUserReview(dto);
//		rttr.addAttribute("meetupId", dto.getMeetupId());
//		return "redirect:/meetup/user/detail.do?meetupId=" + dto.getMeetupId();
//	}  // http://localhost:8080/moit/meetup/user/detail.do?meetupId=22
//	
	// 특정모임의 후기목록 조회
	@RequestMapping(value="/review/test", method=RequestMethod.GET)
	public String selectUserReview(@RequestParam("meetupId") int meetupId, Model model) {
		List<ReviewDto> selectUserReview = reviewservice.selectUserReview(meetupId);
		model.addAttribute("selectUserReview", selectUserReview);
		return "review/test";
	}
	
	// 마이페이지 나의 후기 목록 조회
	@RequestMapping(value="/review/mypage", method=RequestMethod.GET)
	public String selectMyReviewTest(
			Model model, 
			@RequestParam(value="memberId", required=false, defaultValue="10") int memberId,
			@RequestParam(value="sort", required=false, defaultValue="latest") String sort) {		
		
		List<ReviewDto> selectUserReview = reviewservice.selectReviewsByMemberId(memberId, sort);
		model.addAttribute("selectUserReview", selectUserReview);
		model.addAttribute("menu", "review");
		return "review/mypage"; 
	}
	
	// 마이페이지 나의 후기 목록 조회
	@RequestMapping(value="/review/mypageReview", method=RequestMethod.GET)
	public String mypageReview(
			Model model, 
			@RequestParam(value="memberId", required=false, defaultValue="10") int memberId,
			@RequestParam(value="sort", required=false, defaultValue="latest") String sort) {		
		
		List<ReviewDto> selectUserReview = reviewservice.selectReviewsByMemberId(memberId, sort);
		model.addAttribute("selectUserReview", selectUserReview);
		model.addAttribute("menu", "review");
		return "review/mypageReview"; 
	}
	
	//후기 내용 검색
	@RequestMapping(value = "/review/searchContent", method = RequestMethod.GET)
	public String SearchReviewByContent(@RequestParam("Keyword") String Keyword, Model model) {
		List<ReviewDto> searchList = reviewservice.adminSearchReviewByContent(Keyword); 
		model.addAttribute("selectUserReview", searchList); 
		return "review/test";
	}
	
//	@RequestMapping(value="/review/update", method=RequestMethod.GET)
//	public String updateUserReview_GET(ReviewDto dto) {
//        return "redirect:/review/test?meetupId=" + dto.getMeetupId();
//	}
	
	
	
	// 후기 수정 처리 (마이페이지/테스트페이지 동선 구분 리다이렉트)
	@RequestMapping(value="/review/update", method=RequestMethod.POST)
	public String updateUserReview(ReviewDto dto, RedirectAttributes rttr,
								   @RequestParam(value="from", required=false) String from) {
		boolean result = reviewservice.updateUserReview(dto) == 1;
		rttr.addAttribute("updateResult", result);
		
		if ("mypage".equals(from)) {
			return "redirect:/review/mypage?memberId=" + dto.getMemberId();
		}
        return "redirect:/review/test?meetupId=" + dto.getMeetupId();
	}
	
	// 후기 삭제 처리 (마이페이지/테스트페이지 동선 구분 리다이렉트)
	@RequestMapping(value="/review/delete", method=RequestMethod.POST)
	public String deleteUserReview(ReviewDto dto, RedirectAttributes rttr,
								   @RequestParam(value="from", required=false) String from) {
		dto.setMemberId(10); //// 로그인후 유저 멤버아이디 세션에서 불러오기
		boolean result = reviewservice.deleteUserReview(dto) == 1;
		rttr.addAttribute("deleteResult", result);
		
		if ("mypage".equals(from)) {
			return "redirect:/review/mypage?memberId=" + dto.getMemberId();
		}
		return "redirect:/review/test?meetupId=" + dto.getMeetupId();
	}
	
	// 후기 비공개 처리 
	@RequestMapping(value="/review/hide", method=RequestMethod.POST) 
	public String userReviewHide(ReviewDto dto, RedirectAttributes rttr) {
	    int memberId = 10;     
	    dto.setMemberId(memberId);   
	    boolean result = reviewservice.updateUserReviewHide(dto) == 1;	    
	    rttr.addAttribute("hideResult", result);
	    return "redirect:/review/test?meetupId=" + dto.getMeetupId();
	}
	
	//좋아요 기능
	@RequestMapping(value="/review/like", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleReviewLike(
            @RequestParam("id") int reviewId,         
            @RequestParam(value="memberId", required=false, defaultValue="10") int memberId) {
       
        Map<String, Object> responseMap = new HashMap<>();
        
        try {
           
            int latestLikes = reviewservice.toggleLike(reviewId, memberId);
            
          
            responseMap.put("status", "success");
            responseMap.put("latestLikes", latestLikes); 
            return ResponseEntity.ok(responseMap); 
            
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("status", "error");
            return ResponseEntity.status(500).body(responseMap);
        }
    }
	
	
	
	

	// =========================================================================
	// 관리자 기능 영역
	// =========================================================================
	
	// 전체 후기 조회 목록
    @RequestMapping(value = "/admin/review/list", method = RequestMethod.GET)
    public String adminSelectReviewList(Model model, int memberId) {
        List<ReviewDto> adminReviewList = reviewservice.adminSelectReviewList(memberId);
        model.addAttribute("adminReviewList", adminReviewList);
        return "admin/reviews/list";
    }

    // 후기 내용으로 검색
    @RequestMapping(value = "/admin/review/searchContent", method = RequestMethod.GET)
    public String adminSearchReviewByContent(@RequestParam("keyword") String keyword, Model model) {
        List<ReviewDto> searchList = reviewservice.adminSearchReviewByContent(keyword);
        model.addAttribute("adminReviewList", searchList); 
        return "admin/reviews/test";
    }

    // 작성자로 검색
    @RequestMapping(value = "/admin/review/searchWriter", method = RequestMethod.GET)
    public String adminSearchReviewByWriter(@RequestParam("memberId") int memberId, Model model) {
        List<ReviewDto> searchList = reviewservice.adminSearchReviewByWriter(memberId);
        model.addAttribute("adminReviewList", searchList);
        return "admin/review/list";
    }
    
    // 후기 목록조회 (검색기능 뷰 이동)
    @RequestMapping("/review/admin/list.do")
    public String adminListReview (Model model){
    	model.addAttribute("menu", "review");
        return "review/admin/list";
    }

    // 관리자 후기 비공개 처리 
    @RequestMapping(value = "/review/admin/hide", method = RequestMethod.POST)
    @ResponseBody
    public String adminHideReview(@RequestParam("id") int id) {
        int result = reviewservice.adminHideReview(id);
        return result == 1 ? "success" : "fail";
    }

    // 관리자 권한 후기 강제 삭제 처리 
    @RequestMapping(value = "/admin/review/delete", method = RequestMethod.POST)
    @ResponseBody
    public String adminDeleteReview(@RequestParam("id") int id) {
        int result = reviewservice.adminDeleteReview(id);
        return result == 1 ? "success" : "fail";
    }
}