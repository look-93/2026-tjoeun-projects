package com.moit.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import com.moit.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 리뷰 목록 조회 (meetupId 필드 기준)
    List<Review> findByMeetupIdIdAndIsPublicOrderByIdDesc(Long meetupId, String isPublic);

    // [내가 쓴 후기 목록 조회] 키워드 검색 포함
    @Query("SELECT r FROM Review r WHERE r.meetupId.id = :memberId " +
           "AND (:keyword IS NULL OR :keyword = '' OR r.content LIKE %:keyword%) " +
           "ORDER BY r.id DESC")
    List<Review> selectReviewByMemberId(@Param("memberId") Long memberId, @Param("keyword") String keyword);

    // 모임별 후기 기본 조회 (테스트 및 서비스 공용)
    List<Review> findByMeetupIdId(Long meetupId);

    // 회원별 후기 기본 조회
    List<Review> findByMemberIdId(Long memberId);

    // 후기 내용으로 전체 검색
    List<Review> findByContentContainingAndIsPublicOrderByIdDesc(String keyword, String isPublic);

    // 내용 단순 키워드 검색
    List<Review> findByContentContaining(String keyword);

    // 좋아요 수 증가
    @Modifying
    @Query("UPDATE Review r SET r.likesCount = r.likesCount + 1 WHERE r.id = :reviewId")
    int incrementLikesCount(@Param("reviewId") Long reviewId);

    // 좋아요 수 감소
    @Modifying
    @Query("UPDATE Review r SET r.likesCount = r.likesCount - 1 WHERE r.id = :reviewId")
    int decrementLikeCount(@Param("reviewId") Long reviewId);
}