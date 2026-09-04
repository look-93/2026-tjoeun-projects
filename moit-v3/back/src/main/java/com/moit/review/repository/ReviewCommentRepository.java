package com.moit.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.review.entity.ReviewComment;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    @EntityGraph(attributePaths = {"member", "children", "children.member"})
    List<ReviewComment> findByReviewIdAndParentIsNullAndDeleteYnOrderByCreatedAtAsc(Long reviewId, Character deleteYn);

    boolean existsByReviewIdAndMemberIdAndParentIsNullAndDeleteYn(Long reviewId, Long memberId, Character deleteYn);

}