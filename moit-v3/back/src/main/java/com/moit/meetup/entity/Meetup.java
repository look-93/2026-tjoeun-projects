package com.moit.meetup.entity;

import com.moit.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
@Builder
@Table(name="meetups")
public class Meetup extends BaseEntity{ //  extends BaseEntity -> 이렇게하면 공통 컬럼 추가됩니다. 경로 따라가서 확인해보세요.
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long meetupId;
	
	@Column(length = 50)
	private String title;
	
	@Lob
	@Column
	private String content;
	
	@Column
	private Integer maxParticipants;
	
	@Column
	private Integer minParticipants;
	
	@Column
	private Integer sigunguId;
	
	@Column
	private Integer categoryId;
	
	@Column
	private String address;
	
	@Column
	private String addressDetail;
	
	@Column
	private String meetupAt;
	
	@Column
	private String status;
	
	@Column
	private Double latitude;
	
	@Column
	private Double longitude;
	
	@Column
	private Integer nx;
	
	@Column
	private Integer ny;	
	
//	@ManyToOne
//	@JoinColumn(name="member_id", nullable = false)
//	private  Member member;	
	
	
}