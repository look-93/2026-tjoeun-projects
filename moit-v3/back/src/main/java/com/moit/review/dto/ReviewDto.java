package com.moit.review.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.moit.review.entity.Review;
import com.moit.review.entity.ReviewImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReviewDto {

	@Setter
	@Getter
	@NoArgsConstructor
	// 리뷰 작성 ,수정요청
	public static class ReviewRequestDto {
		private Long meetupId;
		private Long memberId;
		private String content;
		private Integer rating;
		private String isPublic;
		private List<Long> imageIds = new ArrayList<>();
	}

	// 리뷰 이미지 응답
	@Getter
	@Setter
	public static class ReviewImageResponseDto {
		private Long reviewImageId;
		private Long imageId;

		public static ReviewImageResponseDto from(ReviewImage reviewImage) {
			ReviewImageResponseDto response = new ReviewImageResponseDto();
			response.setReviewImageId(reviewImage.getId());
			if (reviewImage.getImage() != null) {
				response.setImageId(reviewImage.getImage().getId());
			}
			return response;
		}
	}

	// 리뷰 응답
	@Getter
	@Setter
	public static class ReviewResponseDto {
		private Long id;
		private Long meetupId;
		private Long memberId;
		private String memberNickname;
		private String content;
		private Integer rating;
		private Integer likesCount;
		private Integer viewsCount;
		private String isPublic;
		private List<ReviewImageResponseDto> images = new ArrayList<>();
		private String createdAt;
		private String updatedAt;

		// 목록 조회용 변환 메서드
		public static ReviewResponseDto listFrom(Review review) {
			ReviewResponseDto response = new ReviewResponseDto();
			response.setId(review.getId());
			response.setContent(review.getContent());
			response.setRating(review.getRating());
			response.setLikesCount(review.getLikesCount());
			response.setViewsCount(review.getViewsCount());

			if (review.getMember() != null) {
				response.setMemberId(review.getMember().getId());
				response.setMemberNickname(review.getMember().getNickname());
			}
			return response;
		}

		// 상세 조회용 변환 메서드
		public static ReviewResponseDto detailFrom(Review review) {
			ReviewResponseDto response = new ReviewResponseDto();
			response.setId(review.getId());
			response.setContent(review.getContent());
			response.setRating(review.getRating());
			response.setLikesCount(review.getLikesCount());
			response.setViewsCount(review.getViewsCount());

			if (review.getMeetup() != null) {
				response.setMeetupId(review.getMeetup().getId());
			}

			if (review.getMember() != null) {
				response.setMemberId(review.getMember().getId());
				response.setMemberNickname(review.getMember().getNickname());
			}
			if (review.getReviewImages() != null && !review.getReviewImages().isEmpty()) {
				List<ReviewImageResponseDto> imageDtos = new ArrayList<>();
				for (ReviewImage reviewImage : review.getReviewImages()) {
					imageDtos.add(ReviewImageResponseDto.from(reviewImage));
				}
				response.setImages(imageDtos);
			}

			return response;

		}

	}

	@Getter
	@Setter
	public static class ReviewListResponseDto {
		private List<ReviewResponseDto> reviews;
		private Long totalCount;
		private Integer totalPage; // Page.getTotalPages() 리턴타입(int)에 맞춰 Integer로 변경

		// Page<Review>를 받아 DTO로 변환해주는 편의 메서드 추가
		public static ReviewListResponseDto from(Page<Review> reviewPage) {
			ReviewListResponseDto response = new ReviewListResponseDto();
			
			List<ReviewResponseDto> dtoList = reviewPage.getContent().stream()
					.map(ReviewResponseDto::listFrom)
					.collect(Collectors.toList());

			response.setReviews(dtoList);
			response.setTotalCount(reviewPage.getTotalElements());
			response.setTotalPage(reviewPage.getTotalPages());
			
			return response;
		}
	}

}