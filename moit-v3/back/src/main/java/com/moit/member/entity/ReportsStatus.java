package com.moit.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reports_status")
@Getter @Setter
@NoArgsConstructor
public class ReportsStatus {
	
	@Id
	@Column(name = "reports_status_id")
	private long reportsStatusId;
	
	@Column(name = "status_name", nullable = false, unique = true, length = 30)
	private String statusName;
}
