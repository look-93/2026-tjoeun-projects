package com.moit.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.moit.review.dto.ReviewDto;
import com.moit.review.service.ReviewService;

@Controller
@RequestMapping("/admin/review")
public class AdminReviewController {
	 @Autowired
	    ReviewService reviewService;
	 
	 @GetMapping("/list")
	    public String adminReviewList(
	            @RequestParam(value = "keyword", required = false) String keyword,
	            @RequestParam(value = "memberId", required = false, defaultValue = "0") int memberId,
	            Model model) {

	        List<ReviewDto> adminReviewList;
	        
	       
	        

	        if (keyword != null && !keyword.isBlank()) {
	            // 내용 검색 키워드가 들어온 경우
	            adminReviewList = reviewService.adminSearchReviewByContent(keyword);
	        } else if (memberId > 0) {
	            // 특정 회원 번호로 검색한 경우
	            adminReviewList = reviewService.adminSearchReviewByWriter(memberId);
	        } else {
	            // 아무 조건도 없을 때 (전체 조회용으로 memberId=0 전달)
	            adminReviewList = reviewService.adminSelectReviewList(memberId);
	        }

	        model.addAttribute("adminReviews", adminReviewList);
	        model.addAttribute("keyword", keyword);
	        model.addAttribute("memberId", memberId);

	        return "admin/review/adminList";
	    }
	 
	 // 2. 후기 숨김 처리
	    @PostMapping("/hide")
	    public String adminHideReview(@RequestParam("reviewId") int reviewId) {
	        reviewService.adminHideReview(reviewId);
	        return "redirect:/admin/review/list";
	    }

	    // 3. 후기  삭제
	    @PostMapping("/delete")
	    public String adminDeleteReview(@RequestParam("reviewId") int reviewId) {
	        reviewService.adminDeleteReview(reviewId);
	        return "redirect:/admin/review/list";
	    }

}
