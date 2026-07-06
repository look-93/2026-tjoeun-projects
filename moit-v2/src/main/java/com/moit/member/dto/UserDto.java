package com.moit.member.dto;

import lombok.Data;

@Data
public class UserDto {
	private int memberId;
	private String  loginId;
	private String  mobile;
	private String  nickname;
	private String  email;
	private String  password;
	private String  profileUrl;
	
	private int memberTypeId;
	private int statusId;
	
	private String createdAt;
	private String updatedAt;
	private String deleteYn;
	
	private String provider;
	private String providerId;
}
