package com.moit.meetup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.meetup.entity.MeetupApplication;

@Repository
public interface MeetupApplicationRepository extends JpaRepository<MeetupApplication, Long>{
	
	//모임 신청 조회
	Optional<MeetupApplication> findByMeetup_IdAndMember_Id(Long meetupId, Long memberId);
	
	//마이페이지 내가 신청한 모집글 목록 조회(페이징)
	Page<MeetupApplication> findByMember_Id(Long memberId, Pageable pageable);	
	
	//신청자 목록 조회
	Page<MeetupApplication>findByMeetup_IdAndMeetup_Member_Id(Long meetupId, Long memberId, Pageable pageable);
}
