package com.moit.meetup.entity;

import com.moit.util.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(name="meetup_applications")
public class MeetupApplication extends BaseEntity{

	private Long applicationId;
	private String status;
	private String rejectReason;
	
	@ManyToOne
	@JoinColumn(name="member_id", nullable = false)
	private Meetup meetup;
	
//	@ManyToOne
//	@JoinColumn(name="member_id", nullable = false)
//	private  Member member;	
}
