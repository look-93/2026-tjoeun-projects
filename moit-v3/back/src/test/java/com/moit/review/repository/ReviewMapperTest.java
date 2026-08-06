package com.moit.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.review.entity.Review;
import com.moit.review.entity.ReviewImage;
import com.moit.review.entity.ReviewLike;

@SpringBootTest
public class ReviewMapperTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewImageRepository reviewImageRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    // =========================================================
    // 실제 Oracle DB 조회 테스트
    // =========================================================

    @Test
    @DisplayName("리뷰 Repository - 전체 리뷰 DB 조회")
    void selectAllReviewsTest() {
        List<Review> reviews = reviewRepository.findAll();

        assertThat(reviews).isNotNull();

        System.out.println("===== REVIEWS =====");
        System.out.println("리뷰 데이터 개수 : " + reviews.size());

        reviews.forEach(review ->
            System.out.println(
                "reviewId = " + review.getId()
                + ", content = " + review.getContent()
                + ", rating = " + review.getRating()
            )
        );
    }

    @Test
    @DisplayName("리뷰 이미지 Repository - 전체 이미지 매핑 DB 조회")
    void selectAllReviewImagesTest() {
        List<ReviewImage> reviewImages = reviewImageRepository.findAll();

        assertThat(reviewImages).isNotNull();

        System.out.println("===== REVIEW IMAGES =====");
        System.out.println("리뷰 이미지 매핑 개수 : " + reviewImages.size());

        reviewImages.forEach(ri ->
            System.out.println(
                "reviewImageId = " + ri.getId()
                + ", reviewId = " + (ri.getReview() != null ? ri.getReview().getId() : "null")
            )
        );
    }

    @Test
    @DisplayName("리뷰 좋아요 Repository - 전체 좋아요 DB 조회")
    void selectAllReviewLikesTest() {
        List<ReviewLike> reviewLikes = reviewLikeRepository.findAll();

        assertThat(reviewLikes).isNotNull();

        System.out.println("===== REVIEW LIKES =====");
        System.out.println("좋아요 데이터 개수 : " + reviewLikes.size());

        reviewLikes.forEach(like ->
            System.out.println(
                "likeId = " + like.getId()
            )
        );
    }
}