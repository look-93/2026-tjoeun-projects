package com.moit.review.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.review.dao.ReviewMapper;
import com.moit.review.dto.ReviewDto;


@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired ReviewMapper reviewmapper;
	
	
	//사용자
	@Override
	@Transactional
	public int insertUserReview(ReviewDto dto) {		
		return reviewmapper.insertUserReview(dto);
	}

	@Override
	public List<ReviewDto> selectUserReview(int meetupId, String sort) {
		return reviewmapper.selectUserReview(meetupId,sort);
	}

	@Override
	@Transactional
	public int updateUserReview(ReviewDto dto) {
		return reviewmapper.updateUserReview(dto);
	}

	@Override
	@Transactional
	public int deletUserReview(ReviewDto dto) {
		return reviewmapper.deletUserReview(dto);
	}

	@Override
	@Transactional
	public int updateUserReviewHide(ReviewDto dto) {	
		return reviewmapper.updateUserReviewHide(dto);
	}

	@Override
	public List<ReviewDto> selectReviewByMemberId(int memberId, String sort) {
		return reviewmapper.selectReviewByMemberId(memberId, sort);
	}

	@Override
	public List<ReviewDto> selectReviewByContent(String keyword) {	
		return reviewmapper.selectReviewByContent(keyword);
	}
	
	//좋아요 기능 매퍼와 1:1매칭
	
	@Override
	public int checkLikeExists(Map<String, Object> params) {	
		return reviewmapper.checkLikeExists(params);
	}

	@Override
	@Transactional
	public void insertLike(Map<String, Object> params) {
		reviewmapper.insertLike(params);
		
	}

	@Override
	@Transactional
	public void deleteLike(Map<String, Object> params) {
		reviewmapper.deleteLike(params);
		
	}

	@Override
	@Transactional
	public void incrementLikeCount(int reviewId) {
		reviewmapper.incrementLikeCount(reviewId);
		
	}

	@Override
	public void decrementLikeCount(int reviewId) {
		reviewmapper.decrementLikeCount(reviewId);
	}

	@Override
	public int getLikeCount(int reviewId) {
		return reviewmapper.getLikeCount(reviewId);
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
	@Transactional
	public int adminHideReview(int id) {
		return reviewmapper.adminHideReview(id);
	}

	@Override
	@Transactional
	public int adminDeleteReview(int id) {
		return reviewmapper.adminDeleteReview(id);
	}

}
