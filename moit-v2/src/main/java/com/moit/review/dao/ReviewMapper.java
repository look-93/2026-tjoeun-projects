package com.moit.review.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.moit.review.dto.ReviewDto;

@Mapper
public interface ReviewMapper {
	
	//사용자
	public int insertUserReview(ReviewDto dto);
	public List<ReviewDto> selectUserReview(@Param("meetupId") int meetupId, @Param("sort") String sort);
	//public List<ReviewDto>selectReviewPopular();
	public int updateUserReview(ReviewDto dto);
	public int deletUserReview(ReviewDto dto);
	public int updateUserReviewHide(ReviewDto dto);
	public List<ReviewDto> selectReviewByMemberId(@Param("memberId")int memberId,@Param("sort")String sort);
	public List<ReviewDto> selectReviewByContent(String Keyword);
	
	//좋아요 기능
	public int  checkLikeExists(Map<String,Object>params);
	public void insertLike(Map<String, Object> params);
	public void deleteLike(Map<String, Object> params);
	public void incrementLikeCount(int reviewId);
	public void decrementLikeCount(int reviewId);
	public int  getLikeCount(int reviewId);
	
	//관리자
    public List<ReviewDto>adminSelectReviewList(int memberId);
	public List<ReviewDto>adminSearchReviewByContent(String keyword);
	public List<ReviewDto>adminSearchReviewByWriter (int memberId);
	public int adminHideReview(int id);
	public int adminDeleteReview(int id);

	
	
}
