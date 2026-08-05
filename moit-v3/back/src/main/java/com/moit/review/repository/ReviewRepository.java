package com.moit.review.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.moit.review.entity.Review;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
 
	
	//리뷰 목록 조회(최신순)
	List<Review>findByMeetup_IdAndDeleteYnAndIsPublicOrderByReviewIdDesc(Long meetupId,String deleteYn,String isPublic);
	
	//리뷰 목록 조회(좋아요순)
	List<Review>findByMeetup_IdAndIsPublicOrderByLikesCountDescIdDesc(Long meetupId,String isPublic);
	
	//[내가 쓴 후기 목록 조회] 후기 키워드 검색
	@Query("SELECT r FROM Review r WHERE r.member.id = :memberId " +
	           "AND (:keyword IS NULL OR :keyword = '' OR r.content LIKE %:keyword%) " +
	           "ORDER BY r.id DESC")
	List<Review>SelectReviewByMemerId(@Param("memberId")Long memberId,@Param("keyword")String keyword);
	
	//후기 내용으로 전체 검색
	List<Review>findByContentContainingAndIsPublicOrderByIdDesc(String keyword,String isPublic);
	
	//좋아요 수 증가 특정후기글 좋아요 수
	@Modifying
	@Query("UPDATE Review r SET r.likesCount = r.likesCount + 1 WHERE r.id = :reviewId")
	int incrementLikesCount(@Param("reviewId") Long reviewId);
	
	//좋아요 수 감소 특정후기글 좋아요 수
	@Modifying
	@Query("UPDATE Review r SET r.likesCount = r.likesCount - 1 WHERE r.id = :reviewId")
	int decrementLikeCount(@Param("reviewId") Long reviewId);
	
	
	
}
