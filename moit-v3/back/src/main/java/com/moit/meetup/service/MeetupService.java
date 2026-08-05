package com.moit.meetup.service;

import org.springframework.data.domain.Pageable;

import com.moit.meetup.dto.MeetupDto.MeetupListResponseDto;
import com.moit.meetup.dto.MeetupDto.MeetupRequestDto;
import com.moit.meetup.dto.MeetupDto.MeetupResponseDto;

public interface MeetupService {
	//목록조회
	public MeetupListResponseDto search(Pageable pageable);
	
	//상세조회
	public MeetupResponseDto detail(Long meetupId, Long memberId);
	
	//저장
	public void create(MeetupRequestDto meetupRequest, Long memberId);
	
	//수정
	public void update(MeetupRequestDto meetupRequest, Long meetupId);
	
	//삭제
	public void delete(Long meetupId);	
	
	//모임신청
	
	//좋아요
	
	//모집글 삭제(관리자, 사용자)
	
	//모집글 비공개(관리자)
	
	//마이페이지 내 모집글 조회
	
	//마이페이지 내 모집글 신청자조회
	
	//마이페이지 승인, 거절(거절사유), 노쇼 처리
	
	//마이페이지 내 신청글 조회
 	
}
