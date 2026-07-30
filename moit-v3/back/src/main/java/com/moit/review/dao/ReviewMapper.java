package com.moit.review.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.moit.review.dto.ReviewDto;
import com.moit.util.UtilPaging;

@Mapper
public interface ReviewMapper {
	
	//사용자
	public int insertUserReview(ReviewDto dto);
	public List<ReviewDto> selectUserReview(@Param("meetupId") int meetupId, @Param("sort") String sort);
	//public List<ReviewDto>selectReviewPopular();
	public int updateUserReview(ReviewDto dto);
	public int deleteUserReview(ReviewDto dto);
	public int updateUserReviewHide(ReviewDto dto);
	// 마이페이지 목록 + 검색
	public List<ReviewDto> selectReviewByMemberId(
	        @Param("memberId") int memberId,
	        @Param("keyword") String keyword,
	        @Param("sort") String sort);

	// 모임 상세 검색
	public List<ReviewDto> selectReviewByContent(
	        @Param("meetupId") int meetupId,
	        @Param("keyword") String keyword,
	        @Param("sort") String sort);
	//이미지
	public int insertReviewImage(@Param("reviewId") int reviewId,
            @Param("imageId") int imageId);
	public int insertImage(ReviewDto dto);
	
	
	//좋아요 기능
	public int  checkLikeExists(Map<String,Object>params);
	public void insertLike(Map<String, Object> params);
	public void deleteLike(Map<String, Object> params);
	public void incrementLikeCount(int reviewId);
	public void decrementLikeCount(int reviewId);
	public int  getLikeCount(int reviewId);
	public ReviewDto selectReviewById(int reviewId);
	
	//관리자
    public List<ReviewDto>adminSelectReviewList(int memberId);
	public List<ReviewDto>adminSearchReviewByContent(String keyword);
	public List<ReviewDto>adminSearchReviewByWriter (int memberId);
	public int adminHideReview(@Param("reviewId")int reviewId);
	public int adminDeleteReview(@Param("reviewId")int reviewId);
	

	//관리자 페이징 처리
	public List<ReviewDto> adminGetReviewList(
	        @Param("keyword") String keyword,
	        @Param("memberId") int memberId,
	        @Param("paging") UtilPaging paging,
	        @Param("endRow") int endRow);
	
	
	//검색 조건 맞춰서
	public int adminGetReviewCount(@Param("keyword") String keyword, 
            @Param("memberId") int memberId);
	
}
