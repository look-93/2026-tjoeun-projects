package com.moit.service;

import java.util.List;
import java.util.Map;

import com.moit.dto.ReviewDto;

public interface ReviewService {
	    //사용자
		public int insertUserReview (ReviewDto dto);
		public List<ReviewDto>selectUserReview (int meetupId);
		public List<ReviewDto>selectReviewPopular();
		public int updateUserReview(ReviewDto dto);
		public int deleteUserReview(ReviewDto dto);
		public int updateUserReviewHide(ReviewDto dto);
		public List<ReviewDto> selectReviewsByMemberId(int memberId,String sort);
		public List<ReviewDto> SearchReviewByContent(String keyword);
		
		
		//좋아요 기능
		public int toggleLike(int reviewId, int memberId);
		
		//관리자
		public List<ReviewDto>adminSelectReviewList(int memberId);
		public List<ReviewDto>adminSearchReviewByContent(String keyword);
		public List<ReviewDto>adminSearchReviewByWriter (int memberId);
		public int adminHideReview(int id);
		public int adminDeleteReview(int id);
	
	
		
	
		
}
