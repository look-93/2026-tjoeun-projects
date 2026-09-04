package com.moit.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moit.review.dto.ReviewNotificationResponseDto;
import com.moit.review.service.ReviewNotificationService;
import com.moit.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/reviews")
public class ReviewNotificationController {
	
	private final ReviewNotificationService notificationService;
	
	private Long extractMemberId(Authentication authentication) {
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
			try {
				if (userDetails.getUser() != null) {
					Long memberId = userDetails.getUser().getMemberId();
					if (memberId != null) {
						return memberId;
					}
				}
			} catch (Exception e) {
				log.error("memberId 추출 중 예외 발생: {}", e.getMessage(), e);
			}
		}
		
		throw new IllegalArgumentException("로그인 정보가 유효하지 않습니다. 다시 로그인해 주세요.");
	}
	
	@GetMapping
	public ResponseEntity<List<ReviewNotificationResponseDto>> getMyNotifications(Authentication authentication) {
		Long memberId = extractMemberId(authentication);
		List<ReviewNotificationResponseDto> response = notificationService.getMyNotifications(memberId);
		return ResponseEntity.ok(response);
	}
	
	//알림 읽음 처리   
    @PatchMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable("id") Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("알림이 읽음 처리되었습니다.");
    }

}