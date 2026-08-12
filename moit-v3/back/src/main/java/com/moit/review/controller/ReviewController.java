package com.moit.review.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moit.review.dto.ReviewDto.ReviewListResponseDto;
import com.moit.review.dto.ReviewDto.ReviewRequestDto;
import com.moit.review.dto.ReviewDto.ReviewResponseDto;
import com.moit.review.service.ReviewService;
import com.moit.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Review Api", description = "리뷰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 인증 객체에서 memberId 추출 공통 메서드
    private Long extractMemberId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Long memberId = userDetails.getUser().getMemberId();
            return memberId != null ? memberId.longValue() : null;
        }
        return null;
    }

    @Operation(summary = "리뷰 작성", description = "새로운 리뷰를 작성합니다.")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ReviewRequestDto requestDto, Authentication authentication) {
        Long memberId = extractMemberId(authentication);
        reviewService.create(requestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "리뷰 상세 조회", description = "리뷰 단건을 상세 조회합니다.")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> detail(@PathVariable("reviewId") Long reviewId) {
        ReviewResponseDto response = reviewService.detail(reviewId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> update(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewRequestDto requestDto,
            Authentication authentication) {
        Long memberId = extractMemberId(authentication);
        reviewService.update(requestDto, memberId, reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제(논리 삭제)합니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        Long memberId = extractMemberId(authentication);
        reviewService.delete(reviewId, memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "특정 모임의 리뷰 목록 조회", description = "특정 모임에 작성된 리뷰 목록을 조회합니다.")
    @GetMapping("/meetup/{meetupId}")
    public ResponseEntity<ReviewListResponseDto> getReviewsByMeetup(
            @PathVariable("meetupId") Long meetupId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ReviewListResponseDto response = reviewService.getReviewsByMeetup(meetupId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 작성한 리뷰 목록 조회", description = "마이페이지에서 내가 작성한 리뷰 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<ReviewListResponseDto> getMyReviews(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Long memberId = extractMemberId(authentication);
        ReviewListResponseDto response = reviewService.getMyReviews(memberId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 좋아요 토글", description = "리뷰 좋아요를 등록하거나 취소합니다.")
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Void> reviewLike(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        Long memberId = extractMemberId(authentication);
        reviewService.reviewLike(memberId, reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "AI 리뷰 분석", description = "모임 리뷰 내용을 바탕으로 AI 분석 결과를 반환합니다.")
    @PostMapping("/meetup/{meetupId}/analysis")
    public ResponseEntity<String> reviewAnalysis(@PathVariable("meetupId") Long meetupId) {
        String result = reviewService.reviewAnalysis(meetupId);
        return ResponseEntity.ok(result);
    }


}