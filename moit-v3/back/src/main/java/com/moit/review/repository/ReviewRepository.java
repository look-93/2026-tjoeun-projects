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

    // 1. 모임별 공개 후기 목록 조회 (Review.meetup.id 기준)
    List<Review> findByMeetupIdAndIsPublicOrderByIdDesc(Long meetupId, String isPublic);

    // 2. [내가 쓴 후기 목록 조회] 키워드 검색 포함
    // (기존 r.meetupId.id 오타 수정 -> r.member.id)
    @Query("SELECT r FROM Review r WHERE r.member.id = :memberId " +
           "AND (:keyword IS NULL OR :keyword = '' OR r.content LIKE %:keyword%) " +
           "ORDER BY r.id DESC")
    List<Review> selectReviewByMemberId(@Param("memberId") Long memberId, @Param("keyword") String keyword);

    // 3. 모임별 후기 기본 조회
    List<Review> findByMeetupId(Long meetupId);

    // 4. 회원별 후기 기본 조회
    List<Review> findByMemberId(Long memberId);

    // 5. 후기 내용으로 전체 검색
    List<Review> findByContentContainingAndIsPublicOrderByIdDesc(String keyword, String isPublic);

    // 6. 내용 단순 키워드 검색
    List<Review> findByContentContaining(String keyword);

    // 7. 좋아요 수 증가
    @Modifying
    @Query("UPDATE Review r SET r.likesCount = r.likesCount + 1 WHERE r.id = :reviewId")
    int incrementLikesCount(@Param("reviewId") Long reviewId);

    // 8. 좋아요 수 감소
    @Modifying
    @Query("UPDATE Review r SET r.likesCount = r.likesCount - 1 WHERE r.id = :reviewId")
    int decrementLikeCount(@Param("reviewId") Long reviewId);
}