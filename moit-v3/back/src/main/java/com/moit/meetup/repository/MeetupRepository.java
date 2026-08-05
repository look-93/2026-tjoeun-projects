package com.moit.meetup.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.moit.meetup.dto.MeetupCountResponseDto;
import com.moit.meetup.entity.Meetup;

@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Long>{
	Page<Meetup> findAll(Pageable pageable);
	
	List<Meetup> findByMember_Id(Long memberId);
	
	//관리자 통계
	@Query("""
			SELECT new com.moit.meetup.dto.MeetupCountDto(
			    COUNT(CASE WHEN m.meetupStatus  = com.moit.meetup.enums.MeetupStatus.RECRUITING THEN 1 END),
			    COUNT(CASE WHEN m.meetupStatus  = com.moit.meetup.enums.MeetupStatus.COMPLETED THEN 1 END),
			    COUNT(CASE WHEN m.meetupStatus  = com.moit.meetup.enums.MeetupStatus.CANCELED THEN 1 END),
			    COUNT(CASE WHEN m.meetupStatus  = com.moit.meetup.enums.MeetupStatus.WEATHER_CANCELED THEN 1 END)
			)
			FROM Meetup m
			WHERE m.deleteYn = 'N'
			""")
	MeetupCountResponseDto getMeetupCount();	
}
