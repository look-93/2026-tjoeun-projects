package com.moit.meetup.service;

import org.springframework.data.domain.Pageable;

import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplicationRequestDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplyMemberListResponseDto;
import com.moit.meetup.dto.MeetupApplicationDto.MyApplicationListResponseDto;
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
	public void meetupApply(Long memberId, Long meetupId);
	
	//좋아요
	public void meetupLike(Long meetupId, Long memberId);
	
	//모집글 비공개(관리자)
	public void disableMeetup(Long meetupId);
	
	//마이페이지 내가 신청한 모집글 목록 조회(페이징)
	public MyApplicationListResponseDto getMyApplications(Long memberId);
	
	//마이페이지 내 모집글 신청자 리스트(페이징)
	public MeetupApplyMemberListResponseDto getMyMeetupApplicants(Long meetupId);
	
	//마이페이지 승인, 거절(거절사유), 노쇼 처리
	public void updateApplicationStatus(MeetupApplicationRequestDto requestDto);
	
	//마이페이지 내가 모집한 모집글 조회(페이징)
	public MyApplicationListResponseDto getMyMeetups(Long memberId);
 	
}
