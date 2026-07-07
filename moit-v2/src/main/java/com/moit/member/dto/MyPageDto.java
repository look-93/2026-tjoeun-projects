package com.moit.member.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MyPageDto {
	private Integer memberId;
	private String loginId;
	private String nickname;
	private String email;
	private String mobile;
	private String profileUrl;
	private Integer point;
	private Integer trustScore;
	private LocalDate birth;
	private String gender;
	private String provider;
}
