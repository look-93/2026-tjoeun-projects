package com.moit.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LoginHistroy {
	
	@Id
	@Column(name = "login_history_id")
	private Long loginHistoryId;
}
