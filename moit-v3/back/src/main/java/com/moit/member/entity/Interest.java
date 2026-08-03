package com.moit.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "interest")
@Getter @Setter
@NoArgsConstructor
public class Interest {
	
	@Id
	@Column(name = "interest_id")
	private long interestId;
	
	@Column(name = "interest_name",nullable = false)
	private String interestName;
}
