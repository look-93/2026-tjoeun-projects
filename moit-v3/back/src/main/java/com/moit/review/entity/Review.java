package com.moit.review.entity;

import com.moit.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;

@Entity
@Getter

public class Review  extends BaseEntity{
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false)
	private Long reviewId;
	
	//모임번호
	@Column(nullable=false)
	private Long meetupId;
	
	//작성자 번호
	@Column(nullable=false)
	private Long memberId;
	
	//후기 내용
	@Lob
	@Column(nullable=false)
	private String content;
	
	//별점
	@Column
	private Integer rating;
	
	//좋아요수
	@Column
    private Integer likesCount=0;
	
	//조회수
	@Column
	private Integer viewsCount=0;
	
	//공개여부
	@Column(length=1)
	private String ispublic="Y";
	
	
}
