package com.moit.review.dto;

import lombok.Data;

@Data
public class ReviewDto {
	private int reviewId;
	private int meetupId;
	private int memberId;
	private String content;
	private int rating;
	private int likesCount;
	private int viewsCount;
	private String isPublic;
	private boolean deleteYn;
	private String createdAt;
	private String updatedAt;
}
