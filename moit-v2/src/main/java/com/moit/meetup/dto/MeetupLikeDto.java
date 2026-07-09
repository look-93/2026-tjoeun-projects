package com.moit.meetup.dto;

import lombok.Data;

@Data
public class MeetupLikeDto {
	// 좋아요
	private Integer meetupId;
	private Integer memberId ;
}