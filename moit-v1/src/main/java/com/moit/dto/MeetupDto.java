package com.moit.dto;

import lombok.Data;

@Data
public class MeetupDto {
	private int meetupId;
	private int memberId;
	private String title;
	private String content;
	private int maxParticipants;
	private int minParticipants;
	private int sigunguId;
	private int categoryId;
	private String address;
	private String meetupAt;
	private String deleteYn;
	private String createdAt;
	private String updatedAt;
	private String status;
	private String categoryName;
	
	private int meetupNo;
	private int totalParticipants; 	
	private String imagePath;			
	private String sidoName;		
	private String sigunguName;		
	private int participant; 		
	private String fomatMeetupAt;
	private String fomatcreatedAt;
  
	private int meetupCount; //내모집수
	private int reviewCount; //후기수
	private int likeCount;   //좋아요수
	private int applicationCount; //신청수 
	
	private int applicationId;
	private int applyMemberId;   //신청자아이디
	private String rejectReason; //신청거절사유
	private String applyStatus;  //신청상태
	private String applyAt;
	private String nickname;
    private int start;
    private int end;
    
   	//좋아요
   	private boolean hasLike;
   	private int likeCnt;
	
}