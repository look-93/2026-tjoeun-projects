package com.moit.review.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moit.review.dto.ReviewDto;
import com.moit.review.dto.ReviewDto.ReviewListResponseDto;
import com.moit.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Admin Review Api",description="관리자-리뷰 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {
	 
	private final ReviewService reviewService;
	
	@Operation(summary="관리자-전체 리뷰 목록 조회 및 검색",description="키워드로 전체 리뷰 목록을 조회합니다.")
	@GetMapping("/admin")
	public ResponseEntity<ReviewListResponseDto>  getAdminReviewList(
			@RequestParam(value="keyword",required=false)String keyword,
			@PageableDefault(size=10,sort="id",direction=Sort.Direction.DESC)Pageable pageable){
		ReviewListResponseDto response=reviewService.getAdminReviewList(keyword,pageable);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary="관리자-전체 리뷰 공개 여부 변경",description="관리자가 리뷰의 공개/비공개 상태를 전환합니다.")
	@PatchMapping("/admin/{reviewId}/visibility")
	public ResponseEntity<Void>changeReviewVisibility(@PathVariable("reviewId") Long reviewId){
		  reviewService.changeReviewVisibility(reviewId);
		  return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "관리자 - 리뷰 강제 삭제", description = "관리자가 리뷰를 강제 논리 삭제 처리합니다.")
    @DeleteMapping("/admin/{reviewId}")
    public ResponseEntity<Void> adminDelete(@PathVariable("reviewId") Long reviewId) {
        reviewService.adminDelete(reviewId);
        return ResponseEntity.noContent().build();
    }
	
	
	



}
