package com.moit.review.entity;

import java.util.ArrayList;
import java.util.List;

import com.moit.meetup.entity.Meetup;
import com.moit.member.entity.Member;
import com.moit.util.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="REVIEWS")
@Builder
@NoArgsConstructor  
@AllArgsConstructor 
public class Review  extends BaseEntity{
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false)
	private Long id;
	
	//모임번호
	@ManyToOne(fetch = FetchType.LAZY) //lazy 필요할 때 가져옴
	@JoinColumn(name="MEETUP_ID" ,nullable=false)
	private Meetup meetup;
	
	//작성자 번호
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MEMBER_ID" ,nullable=false)
	private  Member member;
	
	@OneToMany(mappedBy="review",cascade=CascadeType.ALL,orphanRemoval=true)
	private List<ReviewImage> reviewImages=new ArrayList<>();
	
	
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
	private String isPublic="Y";
	
	
}
