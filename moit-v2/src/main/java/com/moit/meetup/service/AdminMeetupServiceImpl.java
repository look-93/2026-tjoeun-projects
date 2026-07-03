package com.moit.meetup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.meetup.dao.MeetupMapper;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupSearchDto;

@Service
public class AdminMeetupServiceImpl implements AdminMeetupService{
	@Autowired MeetupMapper meetupMapper;

	@Override
	public List<MeetupDto> findAllMeetupBy(int pstartno, MeetupSearchDto meetupSearchDto) {
		meetupSearchDto.setEnd(10);
		meetupSearchDto.setStart((pstartno-1)*10);		
		return meetupMapper.findAllMeetupBy(meetupSearchDto);
	}
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto) {
		return meetupMapper.findAllMeetupCountBy(meetupSearchDto);
	}
	
	
}
