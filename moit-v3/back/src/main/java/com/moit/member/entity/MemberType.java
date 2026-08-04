package com.moit.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member_type")
@Getter @Setter
@NoArgsConstructor
public class MemberType {
	
	@Id
	@Column(name = "member_type_id")
	private long memberTypeId;
	
	@Column(name = "type_name", nullable = false, unique = true, length = 30)
	private String typeName;
}
