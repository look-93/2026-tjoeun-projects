package com.moit.meetup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.meetup.entity.Meetup;
import com.moit.meetup.entity.MeetupApplication;

@Repository
public interface MeetupApplicationRepository extends JpaRepository<MeetupApplication, Long>{
	
	//모임 신청 조회
	Optional<MeetupApplication> findByMeetup_IdAndMember_Id(Long meetupId, Long memberId);
	
	//내가 신청한 모임 목록 조회
	//select * from meetup where memberId = :memberId;
	List<MeetupApplication> findByMember_Id(Long memberId);	
}
