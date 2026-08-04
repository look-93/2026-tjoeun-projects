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
	public void update(MeetupRequestDto meetupRequest, Long id);
	
	//삭제
	public void delete(Long id);
}
