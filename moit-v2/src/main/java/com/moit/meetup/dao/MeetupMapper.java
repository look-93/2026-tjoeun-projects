package com.moit.meetup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupSearchDto;

@Mapper
public interface MeetupMapper {
	
	/* 관리자 */
	
	//관리자 meetup 조회
	public List<MeetupDto> findAllMeetupBy(MeetupSearchDto meetupSearchDto);
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto);
	/* 관리자 */
	
}
