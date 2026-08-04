package com.moit.meetup.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.meetup.entity.Meetup;

@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Long>{
	Page<Meetup> findAll(Pageable pageable);
	
	List<Meetup> findByMember_MemberId(Long memberId);
}
