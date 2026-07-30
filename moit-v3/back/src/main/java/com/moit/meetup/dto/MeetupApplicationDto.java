package com.moit.meetup.dto;

import java.util.List;

import lombok.Data;

@Data
public class MeetupApplicationDto {
	private Integer applicationId;
	private Integer meetupId;
	private Integer memberId;
	private String status;
	private String rejectReason;
	private String deleteYn;
	private String createdAt;
	private String updatedAt;	
	private List<String> statusList;
}


/*
status IN (
    'PENDING',             -- 신청 대기
    'APPROVED',            -- 신청 승인
    'REJECTED',            -- 신청 거절
    'CANCELED',            -- 신청자 취소
    'NOSHOW',              -- 노쇼
    'CANCEL_LAST_MINUTE'   -- 당일/24시간 이내 취소
)
*/