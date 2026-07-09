package com.moit.meetup.dto;

import lombok.Data;

@Data
public class MeetupImageDto {
	private Integer meetupId;
	private Integer imageId;
	private String imagePath;
}
