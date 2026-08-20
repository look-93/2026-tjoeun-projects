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
	public static class ReviewRequestDto {
		private Long meetupId;
		private Long memberId;
		private String content;
		private Integer rating;
		private String isPublic;
		private List<Long> imageIds = new ArrayList<>();
	}

	// 리뷰 이미지 응답 DTO
	@Getter
	@Setter
	public static class ReviewImageResponseDto {
		private Long reviewImageId;
		private Long imageId;
		private String imageUrl; // ★ 이미지 실체 URL 경로 필드 추가

		public static ReviewImageResponseDto from(ReviewImage reviewImage) {
			ReviewImageResponseDto response = new ReviewImageResponseDto();
			response.setReviewImageId(reviewImage.getId());
			
			// LazyLoading 에러 방지 및 Null 체크
			try {
				if (reviewImage.getImage() != null) {
					response.setImageId(reviewImage.getImage().getId());
					
					// ★ Image 엔티티에 작성된 파일 경로/URL 필드 getter로 교체해주세요!
					// 예: getFilePath(), getFileUrl(), getStoreFileName() 등
					// response.setImageUrl(reviewImage.getImage().getFileUrl());
				}
			} catch (Exception e) {
				// 지연 로딩 실패 시 예외를 흡수하여 API 전체 500 에러 방지
			}
			
			return response;
		}
	}

	
	// 리뷰 응답 DTO
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
			private String isPublic; // 
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
				response.setIsPublic(review.getIsPublic()); // 👈 [추가] 공개 여부 매핑!
				
				// 👇 [추가] 목록 조회할 때도 meetupId가 응답에 포함되도록 매핑!
			    if (review.getMeetup() != null) {
			        response.setMeetupId(review.getMeetup().getId());
			    }

				if (review.getMember() != null) {
					response.setMemberId(review.getMember().getId());
					response.setMemberNickname(review.getMember().getNickname());
				}

				// ★ LazyInitializationException 방지용 try-catch
				try {
					if (review.getReviewImages() != null && !review.getReviewImages().isEmpty()) {
						List<ReviewImageResponseDto> imageDtos = new ArrayList<>();
						for (ReviewImage reviewImage : review.getReviewImages()) {
							imageDtos.add(ReviewImageResponseDto.from(reviewImage));
						}
						response.setImages(imageDtos);
					}
				} catch (Exception e) {
					response.setImages(new ArrayList<>());
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
				response.setIsPublic(review.getIsPublic()); // 👈 [추가] 공개 여부 매핑!

				if (review.getMeetup() != null) {
					response.setMeetupId(review.getMeetup().getId());
				}

				if (review.getMember() != null) {
					response.setMemberId(review.getMember().getId());
					response.setMemberNickname(review.getMember().getNickname());
				}

				try {
					if (review.getReviewImages() != null && !review.getReviewImages().isEmpty()) {
						List<ReviewImageResponseDto> imageDtos = new ArrayList<>();
						for (ReviewImage reviewImage : review.getReviewImages()) {
							imageDtos.add(ReviewImageResponseDto.from(reviewImage));
						}
						response.setImages(imageDtos);
					}
				} catch (Exception e) {
					response.setImages(new ArrayList<>());
				}

				return response;
			}
		}

	@Getter
	@Setter
	public static class ReviewListResponseDto {
		private List<ReviewResponseDto> reviews;
		private Long totalCount;
		private Integer totalPage;

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