package com.moit.meetup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.meetup.dao.MeetupMapper;
import com.moit.meetup.dto.AdminMeetupStatusSummaryDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupSearchDto;

@Service
public class AdminMeetupServiceImpl implements AdminMeetupService{
	@Autowired MeetupMapper meetupMapper;

	//관리자 - 목록 조회 + paging
	@Override
	public List<MeetupDto> findAllMeetupBy(int pstartno, MeetupSearchDto meetupSearchDto) {
		int pageSize = 10;
		meetupSearchDto.setEnd(pageSize);
		meetupSearchDto.setStart((pstartno-1)*10);		
		return  meetupMapper.findAllMeetupBy(meetupSearchDto);
	}
	@Override
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto) {
		return meetupMapper.findAllMeetupCountBy(meetupSearchDto);
	}

	// 	//관리자 상단 통계
	@Override
	public AdminMeetupStatusSummaryDto findAdminMeetupStatusSummary() {
		return meetupMapper.findAdminMeetupStatusSummary();
	}
	//관리자 - 모임 리스트 삭제
	@Override
	public int updateMeetupDeleteYn(int meetupId) {
		return meetupMapper.updateMeetupDeleteYn(meetupId);
	}
	
	
	
	
}
