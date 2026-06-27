package com.moit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.dao.ReviewMapper;
import com.moit.dto.ReviewDto;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewMapper reviewmapper;

	@Override
	public int insertUserReview(ReviewDto dto) {
		return reviewmapper.insertUserReview(dto);
	}

	@Override
	public List<ReviewDto> selectUserReview(int meetupId) {
		return reviewmapper.selectUserReview(meetupId);
	}

	@Override
	public List<ReviewDto> selectReviewPopular() {
		return reviewmapper.selectReviewPopular();
	}

	@Override
	public int updateUserReview(ReviewDto dto) {
		return reviewmapper.updateUserReview(dto);
	}

	@Override
	public int deleteUserReview(ReviewDto dto) {
		return reviewmapper.deleteUserReview(dto);
	}

	@Override
	public List<ReviewDto> adminSelectReviewList(int memberId) {
		return reviewmapper.adminSelectReviewList(memberId);
	}

	@Override
	public List<ReviewDto> adminSearchReviewByContent(String keyword) {
		return reviewmapper.adminSearchReviewByContent(keyword);
	}

	@Override
	public List<ReviewDto> adminSearchReviewByWriter(int memberId) {
		return reviewmapper.adminSearchReviewByWriter(memberId);
	}

	@Override
	public int adminHideReview(int id) {
		return reviewmapper.adminHideReview(id);
	}

	@Override
	public int adminDeleteReview(int id) {
		return reviewmapper.adminDeleteReview(id);
	}

	@Override
	public int updateUserReviewHide(ReviewDto dto) {
		return reviewmapper.updateUserReviewHide(dto);
	}

	@Override
	public List<ReviewDto> selectReviewsByMemberId(int memberId, String sort) {
		return reviewmapper.selectReviewsByMemberId(memberId, sort);
	}

	@Override
	public List<ReviewDto> SearchReviewByContent(String keyword) {	
		return reviewmapper.SearchReviewByContent(keyword);
	}

	@Override
	public int toggleLike(int reviewId, int memberId) {
		Map<String, Object> params = new HashMap<>();
		params.put("reviewId", reviewId);
		params.put("memberId", memberId);
		
		int alreadyLiked = reviewmapper.checkLikeExists(params);
		if (alreadyLiked == 0) {
	       
			reviewmapper.insertLike(params);
	     
			reviewmapper.incrementLikeCount(reviewId);
	    } else {
	        
	    	reviewmapper.deleteLike(params);
	      
	    	reviewmapper.decrementLikeCount(reviewId);
	    }
		
		return reviewmapper.getLikeCount(reviewId);
	}

	
	
	

}
