package com.moit.meetup.dto;

import lombok.Data;

@Data
public class AdminMeetupStatusSummaryDto {
	//관리자 페이지 통계
	private Integer totalMeetupCount; //전체 모집글
	private Integer recruitingCount; //모집중
	private Integer closedCount;     //모집마감
	private Integer weatherCanceledCount; //기상악화로 인한 취소
}
