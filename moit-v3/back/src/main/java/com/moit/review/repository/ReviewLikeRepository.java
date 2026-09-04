package com.moit.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.review.entity.ReviewLike;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    
    boolean existsByReview_IdAndMember_Id(Long reviewId, Long memberId);
    
    Optional<ReviewLike> findByReview_IdAndMember_Id(Long reviewId, Long memberId);
    
    void deleteByReview_IdAndMember_Id(Long reviewId, Long memberId);
}