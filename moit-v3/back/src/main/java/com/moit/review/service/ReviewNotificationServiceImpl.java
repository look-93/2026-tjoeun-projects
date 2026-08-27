package com.moit.review.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.review.dto.ReviewNotificationResponseDto;
import com.moit.review.entity.ReviewNotification;
import com.moit.review.repository.ReviewNotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ReviewNotificationServiceImpl implements ReviewNotificationService {
	
	private final ReviewNotificationRepository notificationRepository;
	
	@Override
	public List<ReviewNotificationResponseDto> getMyNotifications(Long memberId) {
		// 1. 특정 회원의 알림 목록을 최신순으로 조회
		List<ReviewNotification> notifications = notificationRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
				
		// 💡 [수정] 필터링을 제거하고 조회된 목록을 그대로 전달합니다!
		return notifications.stream()
				.map(ReviewNotificationResponseDto::from)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional 
	public void markAsRead(Long notificationId) {
		// 1. 알림 존재 여부 확인 및 조회
		ReviewNotification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다. ID: " + notificationId));
		
		if ("Y".equals(notification.getIsRead())) {
			return; 
		}

		notification.setIsRead("Y");
	}

}