package com.moit.meetup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.meetup.entity.MeetupApplication;

@Repository
public interface MeetupApplicationRepository extends JpaRepository<MeetupApplication, Long>{
	Optional<MeetupApplication> findTopByMeetup_IdAndMember_MemberIdOrderByCreatedAtDesc(Long meetupId, Long MemberId);
}
