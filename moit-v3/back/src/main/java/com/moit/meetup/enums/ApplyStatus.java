package com.moit.meetup.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyStatus {
	PENDING("신청 대기"),
	APPROVED("신청 승인"),
	REJECTED("신청 거절"),
	CANCELED("신청자 취소"),
	NOSHOW("노쇼"),
	CANCEL_LAST_MINUTE("당일/24시간 이내 취소");
	private String desc;
}
