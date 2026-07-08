package com.moit.meetup.dto;

import lombok.Data;

@Data
public class MeetupWeatherNotificationDto {
	// 날씨 알림 정보
	private Integer notification_id;
	private Integer meetup_id;
	private Integer member_id;		
	private String mobile;			// 발송 핸드폰번호
	private String message_content; // 실제 발송된 알림 내용
	private String send_status; 	// 'SENT', 'FAILED' (조치 상태)
	private String sent_at; 		// 발송 일시
}
