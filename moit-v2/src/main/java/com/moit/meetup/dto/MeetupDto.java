package com.moit.meetup.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MeetupDto {
    private Integer meetupId;
    private Integer memberId;
    private String title;
    private String content;
    private Integer maxParticipants;
    private Integer minParticipants;
    private Integer sigunguId;
    private Integer categoryId;
    private String address;
    private String addressDetail;
    private String meetupAt;
    private String status;
    private BigDecimal latitude;  		// 위도
    private BigDecimal longitude; 		// 경도    
    private String weatherStatus;		// 감지된 날씨
    private Integer rainProbability;	// 감지된 강수확률 (0~100)
    private String deleteYn;
    private String createdAt;
    private String updatedAt;
    private String applyAt;
    private String applyStatus;
    
    private String nickname;
    private Integer totalParticipants;	// 총 참가 인원
    private Integer likeCnt; //좋아요 수
    private Integer hasIike;
    private String categoryName;
    private String formattedMeetupAt;
    private String sidoName;
    private String sigunguName;
    private String statusName;
    private Integer start;
    private Integer end;
    private String fomatMeetupAt;
    private String fomatcreatedAt;
    private Integer meetupTotalCount;
    private Integer applicationId;
    private String rejectReason;
    
    /*통계*/
    private Integer meetupCount;
    private Integer reviewCount;
    private Integer likeCount;
    private Integer applicationCount;
    
    
}
/*
status In(
	'RECRUITING',       -- 모집 중
	'CLOSED',           -- 모집 마감
	'CANCELED',         -- 모임 취소
	'DELETED',          -- 모임 삭제
	'WEATHER_CANCELED', -- 기상 악화로 인한 취소
	'WEATHER_PENDING'   -- 기상 상태 감지로 인한 대기 상태
)

weather_status IN( 	-- 날씨상태
	'CLEAR',
	'CLOUDY',
	'RAIN',
	'SNOW'
)            
*/


