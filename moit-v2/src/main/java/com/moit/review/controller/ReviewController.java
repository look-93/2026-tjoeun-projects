package com.moit.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.moit.review.dto.ReviewDto;
import com.moit.review.service.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {
	@Autowired
	ReviewService reviewService;

	@GetMapping("/meetup/{meetupId}")
	public String selectUserReview(@PathVariable int meetupId,
	@RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,Model model) {
		List<ReviewDto> reviewList=reviewService.selectUserReview(meetupId,sort);
		model.addAttribute("reviews",reviewList);
		model.addAttribute("meetupId",meetupId);
		
		return "user/meetup/review/reviewList";
		
	}
	
	//마이페이지 리뷰 조회
	@GetMapping("/my-reviews")
    public String selectReviewByMemberId(
            @RequestParam int memberId, 
            @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort, 
            Model model) {
        
        List<ReviewDto> myReviewList = reviewService.selectReviewByMemberId(memberId, sort);
        model.addAttribute("myReviews", myReviewList);
        
        return "user/mypage"; 
    }
	

	
	
}
