package com.moit.review.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.member.dto.UserDto;
import com.moit.review.client.OpenAiReviewService;
import com.moit.review.dto.ReviewDto;
import com.moit.review.service.ReviewService;
import com.moit.security.CustomUserDetails;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;
    @Autowired
	private OpenAiReviewService openAiReviewService;


    // 후기 작성 페이지
    @GetMapping("/meetup/review/insert")
    public String insertUserReview_get() {

        return "user/meetup/review/reviewInsert";
    }


 // 후기 등록
    @PostMapping("/meetup/review/insert")
    public String insertUserReview_post(
            ReviewDto dto,
            @RequestParam(value = "attachedImages", required = false)
            MultipartFile[] attachedImages,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {


        String loginId = null;
        String provider = null;
        UserDto user = null;

        Object principal = authentication.getPrincipal();

        Integer memberId = null;


        if(principal instanceof CustomUserDetails) {

            CustomUserDetails users = (CustomUserDetails) principal;

            user = users.getUser();

            loginId = users.getUser().getLoginId();

            memberId = users.getUser().getMemberId();
        }


        dto.setMemberId(memberId);


        try {

            reviewService.insertUserReview(dto, attachedImages);


        } catch(RuntimeException e) {


            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );


            return "redirect:/meetup/review/insert?meetupId="
                    + dto.getMeetupId();

        }


        return "redirect:/meetup/detail?meetupId="
                + dto.getMeetupId()
                + "#review-section";
    }
    
    // 특정 모임 후기 목록 조회
    @GetMapping("/meetup/review/meetup/{meetupId}")
    public String selectUserReview(
            @PathVariable int meetupId,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<ReviewDto> reviewList =
                reviewService.selectReviewByContent(meetupId, keyword, sort);

        model.addAttribute("reviews", reviewList);
        model.addAttribute("meetupId", meetupId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
       
        return "user/meetup/detail";
    }




    // 마이페이지 내가 작성한 후기 목록 조회
    @GetMapping("/mypage/review")
    public String selectReviewByMemberId(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
            Model model, Authentication authentication) {

        // 시큐리티 적용 후 로그인 회원 id로 변경
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
        //int memberId = 2;

        List<ReviewDto> myReviewList =
                reviewService.selectReviewByMemberId(memberId, keyword, sort);
        
        model.addAttribute("dto" , user); 
        model.addAttribute("myReviews", myReviewList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("menu", "review");

        return "user/mypage/review";
    }





    // 후기 수정 페이지(get)
    @GetMapping("/meetup/review/update/{reviewId}")
    public String updateUserReview_get(
            @PathVariable int reviewId,
            @RequestParam(required = false) int meetupId,
            @RequestParam(required = false) String from,
            Model model) {


        ReviewDto targetReview =
                reviewService.selectReviewById(reviewId);


        if(targetReview != null) {

            model.addAttribute("review", targetReview);
            model.addAttribute("meetupId",
                    targetReview.getMeetupId());
        }


        model.addAttribute("from", from);


        return "user/meetup/review/reviewInsert";
    }
    
    
    @PostMapping("/meetup/review/update")
    public String updateUserReview_post(
            ReviewDto dto,
            @RequestParam(required = false) String from,
            RedirectAttributes rttr) {

        System.out.println("수정 from = " + from);

        try {

            reviewService.updateUserReview(dto);

        } catch (RuntimeException e) {

            rttr.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/meetup/review/update/"
                    + dto.getReviewId()
                    + "?meetupId=" + dto.getMeetupId()
                    + "&from=" + from;
        }

        if ("mypage".equals(from)) {
            return "redirect:/mypage/review";
        }

        return "redirect:/meetup/detail?meetupId=" + dto.getMeetupId();
    }

	




    // 후기 삭제
    @GetMapping("/meetup/review/delete/{reviewId}")
    public String deleteUserReview_get(
            ReviewDto dto,
            RedirectAttributes rttr) {


        reviewService.deleteUserReview(dto);


        rttr.addFlashAttribute(
                "deleteResult",
                "success");


        return "redirect:/meetup/detail?meetupId="
                + dto.getMeetupId();
    }





    // 후기 삭제 POST
    @PostMapping("/meetup/review/delete")
    public String deleteUserReview(
            ReviewDto dto,
            RedirectAttributes rttr,
            @RequestParam(value="from", required=false)
            String from) {


        boolean result =
                reviewService.deleteUserReview(dto) == 1;


        rttr.addFlashAttribute(
                "deleteResult",
                result);



        if("mypage".equals(from)) {

            return "redirect:/mypage/review";
        }



        return "redirect:/meetup/"
                + dto.getMeetupId();
    }
    
    
    //좋아요 기능 (비동기 처리)
    @PostMapping("/meetup/review/like/{reviewId}")
    @ResponseBody
    public Map<String, Object> toggleReviewLike(@PathVariable("reviewId") int reviewId, Authentication authentication) {
        
        Map<String, Object> resultBody = new HashMap<>();
        
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
		
        //int memberId = 2; 
        
        try {
            int updatedLikesCount = reviewService.toggleReviewLike(reviewId, memberId);
            resultBody.put("success", true);
            resultBody.put("likesCount", updatedLikesCount);
        } catch (Exception e) {
            e.printStackTrace();
            resultBody.put("success", false);
            resultBody.put("message", "좋아요 처리 중 오류가 발생했습니다.");
        }
        
        return resultBody;
    }
    
    @PostMapping("/meetup/review/analysis")
    @ResponseBody
	public String activeReviewAiAnalysis(@RequestParam("meetupId") int meetupId) {
		
		// 1. 해당 모임 아이디로 등록된 후기 리스트 전체 조회 
		List<ReviewDto> reviewList = reviewService.selectUserReview(meetupId, "latest");
		
		// 2. 가공되지 않은 후기 리스트를 AI 서비스단으로 넘겨 프롬프트 분석 결과 문자열 수신
		String aiReportResult = openAiReviewService.reviewAnalysis(reviewList);
		
		
		return aiReportResult;
	}

}
