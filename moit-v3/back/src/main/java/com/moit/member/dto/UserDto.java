package com.moit.member.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class UserDto {
	
	// 기존 DTO - MyBatis, 기존 service에서 계속 사용
	private Long memberId;
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
	
	private List<Integer> interestIds;
		
	
}
