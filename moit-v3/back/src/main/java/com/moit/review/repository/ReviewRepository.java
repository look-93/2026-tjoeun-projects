package com.moit.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // MyBatis @Param이 아닌 Spring Data @Param 사용
import org.springframework.stereotype.Repository;

import com.moit.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 1. 특정 모임의 리뷰 목록 조회 (최신순) - ReviewIdDesc -> IdDesc 로 수정
    List<Review> findByMeetupId_IdAndIsPublicOrderByIdDesc(Long meetupId, String isPublic);

    // 2. 특정 모임의 리뷰 목록 조회 (좋아요순)
    List<Review> findByMeetupId_IdAndIsPublicOrderByLikesCountDescIdDesc(Long meetupId, String isPublic);

    // 3. [내가 쓴 후기 목록 조회] 후기 키워드 검색 (엔티티의 memberId 필드명에 맞춤)
    @Query("SELECT r FROM Review r WHERE r.memberId.id = :memberId " +
           "AND (:keyword IS NULL OR :keyword = '' OR r.content LIKE %:keyword%) " +
           "ORDER BY r.id DESC")
    List<Review> selectReviewByMemberId(@Param("memberId") Long memberId, @Param("keyword") String keyword);

    // 4. 후기 내용으로 전체 검색
    List<Review> findByContentContainingAndIsPublicOrderByIdDesc(String keyword, String isPublic);

    // 5. 좋아요 수 증가 (clearAutomatically = true 추가로 영속성 컨텍스트 동기화)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Review r SET r.likesCount = r.likesCount + 1 WHERE r.id = :reviewId")
    int incrementLikesCount(@Param("reviewId") Long reviewId);

    // 6. 좋아요 수 감소
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Review r SET r.likesCount = r.likesCount - 1 WHERE r.id = :reviewId")
    int decrementLikesCount(@Param("reviewId") Long reviewId);
}