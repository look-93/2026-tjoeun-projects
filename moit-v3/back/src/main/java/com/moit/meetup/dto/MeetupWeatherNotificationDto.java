package com.moit.meetup.dto;

import lombok.Data;

@Data
public class MeetupWeatherNotificationDto {
	// 날씨 알림 정보
	private Integer notificationId;
	private Integer meetupId;
	private Integer memberId;		
	private String mobile;			// 발송 핸드폰번호
	private String messageContent; // 실제 발송된 알림 내용
	private String sendStatus; 	// 'SENT', 'FAILED' (조치 상태)
	private String sentAt; 		// 발송 일시
}
