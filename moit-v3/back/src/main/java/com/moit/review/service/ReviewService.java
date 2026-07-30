package com.moit.review.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.moit.review.dao.ReviewMapper;
import com.moit.review.dto.ReviewDto;
import com.moit.util.UtilPaging;

public interface ReviewService {

	//사용자
	 int insertUserReview(ReviewDto dto, MultipartFile[] attachedImages);
	List<ReviewDto> selectUserReview(int meetupId, String sort);
	int updateUserReview(ReviewDto dto);
	int deleteUserReview(ReviewDto dto);
	int updateUserReviewHide(ReviewDto dto);
	List<ReviewDto> selectReviewByMemberId(int memberId,
            String keyword,String sort);

List<ReviewDto> selectReviewByContent(int meetupId,
           String keyword,
           String sort);
    ReviewDto selectReviewById(int reviewId);
  
    //좋아요
    int checkLikeExists(Map<String, Object> params);
    void insertLike(Map<String, Object> params);
    void deleteLike(Map<String, Object> params);
    void incrementLikeCount(int reviewId);
    void decrementLikeCount(int reviewId);
    int getLikeCount(int reviewId);
    
    //관리자ㅣ
    List<ReviewDto> adminSelectReviewList(int memberId);
    List<ReviewDto> adminSearchReviewByContent(String keyword);
    List<ReviewDto> adminSearchReviewByWriter(int memberId);
    int adminHideReview(int id);
    int adminDeleteReview(int id);
    //좋아요 
	int toggleReviewLike(int reviewId, int memberId);
	
	//openai
	public String reviewAnalysis(Integer meetupId);
	
	
	
	
	/*
	 * //페이징 public Map<String,Object>adminGetReviewList(String keyword,int
	 * memberId,int page){
	 * 
	 * int total=reviewMapper.adminGetReviewCount(keyword, memberId); UtilPaging
	 * paging = new UtilPaging(total, page); int endRow = paging.getPstartno() +
	 * paging.getOnepagelist() - 1;
	 * 
	 * List<ReviewDto> reviewList = reviewMapper.adminGetReviewList(keyword,
	 * memberId, paging, endRow);
	 * 
	 * Map<String, Object> result = new HashMap<>(); result.put("reviewList",
	 * reviewList); result.put("paging", paging);
	 * 
	 * return result; }
	 */
	
	
	
	
}
