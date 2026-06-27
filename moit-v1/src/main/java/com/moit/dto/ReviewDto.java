package com.moit.dto;

import lombok.Data;

@Data
public class ReviewDto {
	private int id;
	private int meetupId;
	private int memberId;
	private String content;
	private int rating;
	private int likesCount;
	private String isPublic;
	private boolean deleteYn;
	private String createdAt;
	private int createdBy;
	private String updateAt;
	private int updatedBy;
	
	
}
