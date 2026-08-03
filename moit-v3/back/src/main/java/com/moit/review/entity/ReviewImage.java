package com.moit.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;

@Entity
@Getter
@IdClass(ReviewImageId.class)
public class ReviewImage {
	
	
	//후기번호
	@Id
	@Column(name="review_id",nullable=false)
	private Long reviewId;
	
	//이미지 번호
	@Id
	@Column(name="image_id",nullable=false)
	private Long imageId;
}
