package com.moit.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.review.entity.ReviewComment;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    // 특정 리뷰의 최상위 댓글 목록 조회 (대댓글 제외, 시간순 정렬)
    List<ReviewComment> findByReviewIdAndParentIsNullOrderByCreatedAtAsc(Long reviewId);
}