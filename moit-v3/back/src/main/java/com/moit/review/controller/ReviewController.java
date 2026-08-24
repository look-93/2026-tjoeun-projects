package com.moit.review.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.moit.review.repository.ReviewImageRepository;
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
    private final ReviewImageRepository reviewImageRepository;

    // 인증 객체에서 memberId 추출 공통 메서드 (Fallback 제거 버전)
    private Long extractMemberId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            try {
                System.out.println("=== 인증된 사용자 Principal 객체 확인 ===");
                System.out.println("userDetails: " + userDetails);
                
                if (userDetails.getUser() != null) {
                    Long memberId = userDetails.getUser().getMemberId();
                    System.out.println("추출된 memberId: " + memberId);
                    
                    if (memberId != null) {
                        return memberId;
                    }
                }
            } catch (Exception e) {
                System.out.println("memberId 추출 중 예외 발생: " + e.getMessage());
            }
        }
        
        // 💡 인증 정보가 없거나 추출 실패 시 1L 대신 예외를 던져 잘못된 요청/인증 실패를 알립니다.
        throw new IllegalArgumentException("로그인 정보가 유효하지 않습니다. 다시 로그인해 주세요.");
    }

    @Operation(summary = "리뷰 작성", description = "새로운 리뷰를 작성합니다.")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReviewRequestDto requestDto, Authentication authentication) {
        try {
            Long memberId = extractMemberId(authentication);
            reviewService.create(requestDto, memberId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "리뷰 상세 조회", description = "리뷰 단건을 상세 조회합니다.")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> detail(@PathVariable("reviewId") Long reviewId) {
        ReviewResponseDto response = reviewService.detail(reviewId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewRequestDto requestDto,
            Authentication authentication) {
        try {
            Long memberId = extractMemberId(authentication);
            reviewService.update(requestDto, memberId, reviewId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        
        ReviewListResponseDto response = reviewService.getReviewsByMeetup(meetupId, keyword, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 작성한 리뷰 목록 조회", description = "마이페이지에서 내가 작성한 리뷰 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<ReviewListResponseDto> getMyReviews(
            @RequestParam(value = "keyword", required = false) String keyword,
            Authentication authentication,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Long memberId = extractMemberId(authentication);
        ReviewListResponseDto response = reviewService.getMyReviews(memberId, keyword, pageable); 
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 좋아요 토글", description = "리뷰 좋아요를 등록하거나 취소합니다.")
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Void> reviewLike(@PathVariable("reviewId") Long reviewId, Authentication authentication) {
        System.out.println("=== [LIKE API 요청 수신] ===");
        System.out.println("전달받은 reviewId: " + reviewId);

        Long memberId = extractMemberId(authentication);
        System.out.println("추출된 최종 memberId: " + memberId);

        reviewService.reviewLike(memberId, reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "AI 리뷰 분석", description = "모임 리뷰 내용을 바탕으로 AI 분석 결과를 반환합니다.")
    @PostMapping("/meetup/{meetupId}/analysis")
    public ResponseEntity<String> reviewAnalysis(@PathVariable("meetupId") Long meetupId) {
        System.out.println("===== 🤖 프론트에서 받은 meetupId: " + meetupId + " =====");
        String result = reviewService.reviewAnalysis(meetupId);
        System.out.println("===== 🤖 AI 분석 완료 결과: " + result + " =====");
        return ResponseEntity.ok(result);
    }
}