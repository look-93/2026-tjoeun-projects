package com.moit.member.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserDto {
	private Integer memberId;
	private String  loginId;
	private String  mobile;
	private String  nickname;
	private String  email;
	private String  password;
	private String  profileUrl;
	private String gender;
    private String birth;
    private String joinIp;
	
	private int memberTypeId;
	private int statusId;
	
	private String createdAt;
	private String updatedAt;
	private String deleteYn;
	
	private String provider;
	private String providerId;
	
	private MultipartFile profileImage;
}
