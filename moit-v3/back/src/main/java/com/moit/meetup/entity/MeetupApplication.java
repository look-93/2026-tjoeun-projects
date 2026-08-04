package com.moit.meetup.entity;

import com.moit.member.entity.Member;
import com.moit.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	
	@Column
	private String status;
	
	@Column
	private String rejectReason;
	
	@ManyToOne
	@JoinColumn(name="meetup_id", nullable = false)
	private Meetup meetup;
	
	@ManyToOne
	@JoinColumn(name="member_id", nullable = false)
	private  Member member;	
}
