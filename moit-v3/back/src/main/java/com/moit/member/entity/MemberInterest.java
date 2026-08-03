package com.moit.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member_interest")
@Getter @Setter
@NoArgsConstructor
public class MemberInterest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_interest_seq")
	@SequenceGenerator(name = "member_interest_seq", sequenceName = "member_interest_seq", allocationSize = 1)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Members member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id")
	private Interest interest;
}
