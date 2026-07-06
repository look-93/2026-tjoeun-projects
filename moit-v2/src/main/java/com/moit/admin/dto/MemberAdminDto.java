package com.moit.admin.dto;

import java.time.*;

import lombok.Data;

@Data
public class MemberAdminDto {
	private Integer memberId;
	private String loginId;
	private String nickname;
	private String email;
	private String mobile;
	private String memberType;
	private String memberStatus;
	private String reportStatus;
	private Integer point;
	private Integer trustScore;
	private LocalDate birth;
	private String gender;
	private LocalDateTime createdAt;
}
