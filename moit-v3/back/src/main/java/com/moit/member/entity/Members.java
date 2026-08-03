package com.moit.member.entity;

import com.moit.util.BaseEntity;

import jakarta.persistence.Column;
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
@Table(name = "members")
@Getter @Setter
@NoArgsConstructor
public class Members extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "members_seq")
	@SequenceGenerator(name = "members_seq", sequenceName = "members_seq",allocationSize = 1)
	@Column(name = "member_id")
	private long memberId;
	
	@Column(name = "login_id", nullable = false, unique = true)
	private String loginId;
	
	@Column
	private String mobile;
	
	@Column(nullable = false, unique = true)
	private String nickname;
	
	@Column(unique = false)
	private String password;
	
	@Column(name = "profile_url")
	private String profileUrl;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_type_id")
	private MemberType memberType;
	
}
