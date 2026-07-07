package com.moit.member.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserUpdateDto {
	private String nickname;
	private String mobile;
	private String email;
	private String profileUrl;
	private String gender;
	private LocalDate birth;
}
