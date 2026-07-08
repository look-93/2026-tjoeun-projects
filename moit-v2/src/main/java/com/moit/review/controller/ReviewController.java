package com.moit.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.moit.review.dto.ReviewDto;
import com.moit.review.service.ReviewService;

@Controller
//@RequestMapping("")
public class ReviewController {
   @Autowired
   ReviewService reviewService;
   
   //후기 작성(get)
   @GetMapping("/meetup/review/insert")
   public String insertUserReview_get() {

       return "user/meetup/review/reviewInsert";
   }   
   
   
   
   
   @PostMapping("/meetup/review/insert")
   public String insertUserReview_post(ReviewDto dto) {

       dto.setMemberId(2);
       System.out.println(dto.getMeetupId());
       reviewService.insertUserReview(dto);

       return "redirect:/meetup/detail?meetupId=" + dto.getMeetupId();
   }
   
   
    //후기 모임별 목록
   @GetMapping("/meetup/review/meetup/{meetupId}")
   public String selectUserReview(@PathVariable int meetupId,
         @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort, Model model) {
      List<ReviewDto> reviewList = reviewService.selectUserReview(meetupId, sort);
      model.addAttribute("reviews", reviewList);
      model.addAttribute("meetupId", meetupId);

      return "user/meetup/detail";

   }

   // 마이페이지 리뷰 조회
   @GetMapping("/meetup/review/mypage")
   public String selectReviewByMemberId(@RequestParam int memberId,
         @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort, Model model) {

      List<ReviewDto> myReviewList = reviewService.selectReviewByMemberId(memberId, sort);
      model.addAttribute("myReviews", myReviewList);

      return "user/mypage";
   }

   // 키워드 검색
   @GetMapping("/meetup/review/search")
   public String selectReviewByContent(@RequestParam String keyword, Model model) {
      List<ReviewDto> searchResult = reviewService.selectReviewByContent(keyword);
      model.addAttribute("reviews", searchResult);
      model.addAttribute("keyword", keyword);
      return "user/meetup/review/reviewList";
   }
   //후기 수정(get)
   @GetMapping("/meetup/review/update/{reviewId}")
   public String updateUserReview_get(
           @PathVariable int reviewId, @RequestParam int meetupId,
           Model model) {
       
      
       ReviewDto targetReview = reviewService.selectReviewById(reviewId);
       
       if (targetReview != null) {
           model.addAttribute("review", targetReview);
           model.addAttribute("meetupId", targetReview.getMeetupId());
       }
       
       return "user/meetup/review/reviewInsert";
   }
   
   //후기 수정 post
   @PostMapping("/meetup/review/update")
   public String updateUserReview_post(ReviewDto dto) {  //ReviewDto dto

       // 후기 수정
       reviewService.updateUserReview(dto);

       // 수정한 모임 후기 목록으로 이동
       //return "redirect:/meetup/review/reviewList/" +reviewDto.getMeetupId();
       return "redirect:/meetup/detail?meetupId=" + dto.getMeetupId();
   }
   
   //후기 삭제 get
   @GetMapping("/meetup/review/delete/{reviewId}")
   public String deleteUserReview_get(ReviewDto dto,RedirectAttributes rttr) {
      
      
      reviewService.deleteUserReview(dto);
      rttr.addFlashAttribute("deleteResult", "success");
      return "redirect:/meetup/detail?meetupId=" + dto.getMeetupId();
   }
   
   // 후기 삭제
   @PostMapping("/meetup/review/delete")
   public String deleteUserReview(ReviewDto dto,RedirectAttributes rttr,
         @RequestParam(value="from", required=false) String from) {
      boolean result =reviewService.deleteUserReview(dto)==1;
       rttr.addFlashAttribute("deleteResult",result);
       if ("mypage".equals(from)) {
    	    return "redirect:/meetup/review/mypage?memberId=" + dto.getMemberId();
    	}
       
       return "redirect:/meetup/" + dto.getMeetupId();
   }
   
   

   }   // 비동기 처리 방식


