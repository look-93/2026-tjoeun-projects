package com.moit.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.moit.dto.ReviewDto;

@Mapper
public interface ReviewMapper {
	
	//시용자
	public int insertUserReview (ReviewDto dto);
	public List<ReviewDto>selectUserReview (int meetupId);
	public List<ReviewDto>selectReviewPopular();
	public int updateUserReview(ReviewDto dto);
	public int deleteUserReview(ReviewDto dto);
	public int updateUserReviewHide(ReviewDto dto);
	public List<ReviewDto> selectReviewsByMemberId(@Param("memberId") int memberId, @Param("sort") String sort);
	public List<ReviewDto> SearchReviewByContent(String keyword);
	
	
	//좋아요 기능
	public int checkLikeExists(Map<String, Object> params);
	public void insertLike(Map<String, Object> params);
	public void deleteLike(Map<String, Object> params);
	public void incrementLikeCount(int reviewId);
	public void decrementLikeCount(int reviewId);
	public int getLikeCount(int reviewId);

	
	//관리자
	public List<ReviewDto>adminSelectReviewList(int memberId);
	public List<ReviewDto>adminSearchReviewByContent(String keyword);
	public List<ReviewDto>adminSearchReviewByWriter (int memberId);
	public int adminHideReview(int id);
	public int adminDeleteReview(int id);
	
	
	
}
