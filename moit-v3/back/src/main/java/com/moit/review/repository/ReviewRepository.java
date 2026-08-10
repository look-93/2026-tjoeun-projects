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

    // 1. 특정 모임의 공개 리뷰 목록 조회 (최신순)
    List<Review> findByMeetupId_IdAndIsPublicOrderByIdDesc(Long meetupId, String isPublic);

    // 2. 특정 모임의 공개 리뷰 목록 조회 (좋아요순)
    List<Review> findByMeetupId_IdAndIsPublicOrderByLikesCountDescIdDesc(Long meetupId, String isPublic);

    // 3. 모임별 단순 후기 조회
    List<Review> findByMeetupId_Id(Long meetupId);

    // 4. 회원별 단순 후기 조회
    List<Review> findByMemberId_Id(Long memberId);

    // 5. [내가 쓴 후기 목록 조회] 키워드 검색 포함 (Review 엔티티의 memberId 필드 사용)
    @Query("SELECT r FROM Review r WHERE r.memberId.id = :memberId " +
           "AND (:keyword IS NULL OR :keyword = '' OR r.content LIKE %:keyword%) " +
           "ORDER BY r.id DESC")
    List<Review> selectReviewByMemberId(@Param("memberId") Long memberId, @Param("keyword") String keyword);

    // 6. 공개 후기 내용으로 키워드 검색
    List<Review> findByContentContainingAndIsPublicOrderByIdDesc(String keyword, String isPublic);

    // 7. 내용 단순 키워드 검색
    List<Review> findByContentContaining(String keyword);

    // 8. 좋아요 수 증가 (clearAutomatically = true 추가로 DB 반영 후 영속성 컨텍스트 동기화)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Review r SET r.likesCount = r.likesCount + 1 WHERE r.id = :reviewId")
    int incrementLikesCount(@Param("reviewId") Long reviewId);

    // 9. 좋아요 수 감소
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Review r SET r.likesCount = r.likesCount - 1 WHERE r.id = :reviewId")
    int decrementLikesCount(@Param("reviewId") Long reviewId);
}