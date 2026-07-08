package com.moit.meetup.dao;

import org.apache.ibatis.annotations.Mapper;

import com.moit.meetup.dto.MeetupDto;

@Mapper
public interface MeetupMapper {

    MeetupDto findById(int meetupId);

}