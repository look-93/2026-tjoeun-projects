package com.moit.review.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moit.review.dao.ReviewMapper;
import com.moit.review.dto.ReviewDto;


@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired ReviewMapper reviewmapper;
	
	
	//사용자
	/*
	 * @Override
	 * 
	 * @Transactional public int insertUserReview(ReviewDto dto, MultipartFile[]
	 * attachedImages) {
	 * 
	 * 
	 * int result = reviewmapper.insertUserReview(dto);
	 * 
	 * 
	 * 
	 * return result; }
	 */
	
	
	@Override
	@Transactional
	public int insertUserReview(ReviewDto dto, MultipartFile[] attachedImages) {

	    // 1. 후기 등록
	    int result = reviewmapper.insertUserReview(dto);


	    // 2. 이미지 등록
	    if (attachedImages != null 
	            && attachedImages.length > 0 
	            && !attachedImages[0].isEmpty()) {

	        MultipartFile file = attachedImages[0];

	        try {

	            String uploadDir = "C:/upload/reviews/";

	            String originalFileName = file.getOriginalFilename();

	            String saveFileName = java.util.UUID.randomUUID()
	                    + "_" 
	                    + originalFileName;


	            File folder = new File(uploadDir);

	            if (!folder.exists()) {
	                folder.mkdirs();
	            }


	            file.transferTo(new File(uploadDir + saveFileName));


	            // images 저장
	            dto.setImagePath("/upload/reviews/" + saveFileName);

	            reviewmapper.insertImage(dto);


	            // 값 확인
	            System.out.println("======== 이미지 저장 확인 ========");
	            System.out.println("reviewId : " + dto.getReviewId());
	            System.out.println("imageId : " + dto.getImageId());
	            System.out.println("===============================");


	            // review_images 연결 저장
	            reviewmapper.insertReviewImage(
	                    dto.getReviewId(),
	                    dto.getImageId()
	            );


	        } catch (Exception e) {

	            e.printStackTrace();

	            throw new RuntimeException(
	                    "파일 업로드 또는 DB 저장 실패",
	                    e
	            );
	        }
	    }


	    return result;
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
	public int deleteUserReview(ReviewDto dto) {
		return reviewmapper.deleteUserReview(dto);
	}

	@Override
	@Transactional
	public int updateUserReviewHide(ReviewDto dto) {	
		return reviewmapper.updateUserReviewHide(dto);
	}

	@Override
	public List<ReviewDto> selectReviewByMemberId(int memberId, String keyword, String sort) {
	    return reviewmapper.selectReviewByMemberId(memberId, keyword, sort);
	}

	@Override
	public List<ReviewDto> selectReviewByContent(int meetupId, String keyword, String sort) {
	    return reviewmapper.selectReviewByContent(meetupId, keyword, sort);
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
	
	//////관리자

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
	public int adminHideReview(int reviewId) {
		return reviewmapper.adminHideReview(reviewId);
	}

	@Override
	@Transactional
	public int adminDeleteReview(int reviewId) {
		return reviewmapper.adminDeleteReview(reviewId);
	}

	@Override
	public ReviewDto selectReviewById(int reviewId) {
		
		return reviewmapper.selectReviewById(reviewId);
	}
	
	@Override
	@Transactional
	public int toggleReviewLike(int reviewId, int memberId) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("reviewId", reviewId);
	    params.put("memberId", memberId);

	   
	    int likeExists = reviewmapper.checkLikeExists(params); 

	    if (likeExists > 0) {
	        
	        reviewmapper.deleteLike(params);           
	        reviewmapper.decrementLikeCount(reviewId);  
	    } else {
	        
	        reviewmapper.insertLike(params);           
	        reviewmapper.incrementLikeCount(reviewId);  
	    }

	   
	    return reviewmapper.getLikeCount(reviewId); 
	}

	

}
