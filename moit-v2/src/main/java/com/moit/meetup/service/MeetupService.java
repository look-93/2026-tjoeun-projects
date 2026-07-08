package com.moit.meetup.service;

import org.springframework.stereotype.Service;

import com.moit.meetup.dao.MeetupMapper;
import com.moit.meetup.dto.MeetupDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetupService {

    private final MeetupMapper meetupMapper;

    public MeetupDto getDetail(int meetupId){ return meetupMapper.findById(meetupId); }
}
