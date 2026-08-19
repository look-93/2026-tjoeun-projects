package com.moit.review.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.common.entity.Image;
import com.moit.exception.ResourceNotFoundException;
import com.moit.meetup.entity.Meetup;
import com.moit.meetup.repository.MeetupRepository;
import com.moit.member.entity.Member;
import com.moit.member.repository.MemberRepository;
import com.moit.review.client.ModerationClientService;
import com.moit.review.client.OpenAiReviewService;
import com.moit.review.dto.ReviewDto.ReviewListResponseDto;
import com.moit.review.dto.ReviewDto.ReviewRequestDto;
import com.moit.review.dto.ReviewDto.ReviewResponseDto;
import com.moit.review.entity.Review;
import com.moit.review.entity.ReviewImage;
import com.moit.review.entity.ReviewLike;
import com.moit.review.repository.ReviewImageRepository;
import com.moit.review.repository.ReviewLikeRepository;
import com.moit.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewLikeRepository reviewLikeRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final MeetupRepository meetupRepository;
	private final MemberRepository memberRepository;

	// api 연동
	private final ModerationClientService moderationClientService;
	private final OpenAiReviewService openAiReviewService;

	// 리뷰 작성
	@Override
	@Transactional
	public void create(ReviewRequestDto requestDto, Long memberId) {

		// api 욕설 비방 필터링 연동
		System.out.println("=== 필터링 검사할 내용: " + requestDto.getContent());
		boolean isFlagged = moderationClientService.checkContent(requestDto.getContent());
		System.out.println("=== 필터링 결과(isFlagged): " + isFlagged);
		if (isFlagged) {
			throw new IllegalArgumentException("부적절한 내용(비속어/유해 콘텐츠)이 포함되어 있어 리뷰를 등록할 수 없습니다.");
		}

		Meetup meetup = meetupRepository.getReferenceById(requestDto.getMeetupId());
		Member member = memberRepository.getReferenceById(memberId);

		Review review = Review.builder().meetup(meetup).member(member).content(requestDto.getContent())
				.rating(requestDto.getRating())
				.isPublic(requestDto.getIsPublic() != null ? requestDto.getIsPublic() : "Y").viewsCount(0).build();

		Review savedReview = reviewRepository.save(review);

		if (requestDto.getImageIds() != null && !requestDto.getImageIds().isEmpty()) {
			for (Long imageId : requestDto.getImageIds()) {
				Image image = Image.builder().id(imageId).build();

				ReviewImage reviewImage = ReviewImage.builder().review(savedReview).image(image).build();

				reviewImageRepository.save(reviewImage);
			}
		}
	}

	// 리뷰 상세 조회
	@Override
	@Transactional
	public ReviewResponseDto detail(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 리뷰입니다. REVIEW ID : " + reviewId));

		if ("N".equals(review.getIsPublic())) {
			throw new IllegalArgumentException("비공개 처리된 리뷰입니다.");
		}

		// review.getViewsCount() NPE 방지
		int currentViews = (review.getViewsCount() != null) ? review.getViewsCount() : 0;
		review.setViewsCount(currentViews + 1);

		return ReviewResponseDto.detailFrom(review);
	}

	// 리뷰 수정
	@Override
	@Transactional
	public void update(ReviewRequestDto requestDto, Long memberId, Long reviewId) {
		
		boolean isFlagged = moderationClientService.checkContent(requestDto.getContent());
		if (isFlagged) {
			throw new IllegalArgumentException("부적절한 내용(비속어/유해 콘텐츠)이 포함되어 있어 리뷰를 수정할 수 없습니다.");
		}
		
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 리뷰입니다. REVIEW ID : " + reviewId));

		if (!review.getMember().getId().equals(memberId)) {
			throw new IllegalArgumentException("본인이 작성한 리뷰만 수정할 수 있습니다.");
		}

		review.setContent(requestDto.getContent());
		review.setRating(requestDto.getRating());
		if (requestDto.getIsPublic() != null) {
			review.setIsPublic(requestDto.getIsPublic());
		}

		// ★ [수정] 이미지 업데이트 부분
		if (requestDto.getImageIds() != null) {
			// 1) 기존 DB 연결 정보 완전히 삭제
			reviewImageRepository.deleteByReview_Id(reviewId);
			review.getReviewImages().clear();

			// 2) 새로 넘어온 이미지들 매핑 저장
			for (Long imageId : requestDto.getImageIds()) {
				Image image = Image.builder().id(imageId).build();
				ReviewImage reviewImage = ReviewImage.builder().review(review).image(image).build();
				
				reviewImageRepository.save(reviewImage); // DB에 직접 저장
			}
		}
	}

	// 리뷰 삭제
	@Override
	@Transactional
	public void delete(Long reviewId, Long memberId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 리뷰입니다. REVIEW ID : " + reviewId));

		if (!review.getMember().getId().equals(memberId)) {
			throw new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
		}

		review.setDeleteYn('Y');
	}

	

		@Override
		public ReviewListResponseDto getReviewsByMeetup(Long meetupId, Pageable pageable) {
			return getReviewsByMeetup(meetupId, null, pageable);
		}

		// 2. 검색어(keyword)까지 지원하는 메서드 (상세페이지용)
		@Override
		public ReviewListResponseDto getReviewsByMeetup(Long meetupId, String keyword, Pageable pageable) {
			// 공개된('Y') 리뷰와 검색어(keyword), 페이징/정렬 적용 조회
			Page<Review> reviewPage = reviewRepository.selectReviewByMeetupId(meetupId, keyword, pageable);

			// DTO 변환 시 이미지 정보 지연 로딩 방어 처리
			reviewPage.getContent().forEach(review -> {
				if (review.getReviewImages() != null) {
					review.getReviewImages().forEach(ri -> {
						if (ri.getImage() != null) {
							ri.getImage().getId(); 
						}
					});
				}
			});

			return ReviewListResponseDto.from(reviewPage);
		}

	// 내가 작성한 리뷰 목록 조회 (검색 + 정렬 + 페이징 적용)
		@Override
		public ReviewListResponseDto getMyReviews(Long memberId, String keyword, Pageable pageable) {
			// 1. 레포지토리에서 페이징과 검색, 정렬 조건이 반영된 Page<Review> 객체 조회
			Page<Review> page = reviewRepository.selectReviewByMemberId(memberId, keyword, pageable);

			// 2. DTO 변환 시 이미지 정보 지연 로딩(Lazy Loading) 방어 처리
			page.getContent().forEach(review -> {
				if (review.getReviewImages() != null) {
					review.getReviewImages().forEach(ri -> {
						if (ri.getImage() != null) {
							ri.getImage().getId(); // 지연 로딩 강제 초기화
						}
					});
				}
			});

			// 3. Page 객체를 공통 응답 DTO(ReviewListResponseDto)로 변환하여 반환
			return ReviewListResponseDto.from(page);
		}

	// 리뷰 좋아요 (등록 / 취소 토글)
			@Override
			@Transactional
			public void reviewLike(Long memberId, Long reviewId) {
				boolean exists = reviewLikeRepository.existsByReview_IdAndMember_Id(reviewId, memberId);

				if (exists) {
					reviewLikeRepository.deleteByReview_IdAndMember_Id(reviewId, memberId);
					reviewRepository.decrementLikesCount(reviewId);
					return;
				}

				Review review = reviewRepository.findById(reviewId)
						.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 리뷰입니다. REVIEW ID : " + reviewId));
				
				// ★ [수정] getReferenceById 대신 findById를 사용하여 실제 Member 엔티티를 안전하게 조회합니다.
				Member member = memberRepository.findById(memberId)
						.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 회원입니다. MEMBER ID : " + memberId));

				ReviewLike reviewLike = ReviewLike.builder().review(review).member(member).build();

				reviewLikeRepository.save(reviewLike);
				reviewRepository.incrementLikesCount(reviewId);
			}

	// AI 리뷰 분석
	@Override
	public String reviewAnalysis(Long meetupId) {
		// [수정] "N" (String) -> 'N' (Character) 로 전달
		List<Review> reviewList = reviewRepository.findByMeetup_IdAndDeleteYnAndIsPublicOrderByIdDesc(meetupId, 'N', "Y");

		List<ReviewResponseDto> dtoList = reviewList.stream()
				.map(ReviewResponseDto::listFrom)
				.toList();
		
		return openAiReviewService.reviewAnalysis(dtoList);
	}

	// 관리자 - 전체 리뷰 목록 조회
	@Override
	public ReviewListResponseDto getAdminReviewList(String keyword, Pageable pageable) {
		Page<Review> page = reviewRepository.adminGetReviewList(keyword, null, pageable);

		return ReviewListResponseDto.from(page);
	}

	// 관리자 - 공개 여부 변경
	@Override
	@Transactional
	public void changeReviewVisibility(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 리뷰입니다. REVIEW ID : " + reviewId));

		review.setIsPublic("Y".equals(review.getIsPublic()) ? "N" : "Y");
	}

	// 관리자 - 강제 삭제 (논리 삭제)
	@Override
	@Transactional
	public void adminDelete(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 리뷰입니다. REVIEW ID : " + reviewId));

		review.setDeleteYn('Y');
	}
}