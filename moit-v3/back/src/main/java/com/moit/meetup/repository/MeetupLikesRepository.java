package com.moit.meetup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.meetup.entity.MeetupLike;

@Repository
public interface MeetupLikesRepository extends JpaRepository<MeetupLike, Long>{
	//특정 게시글 좋아요 수 집계
	
	//특정 유저가 특정게시글에 좋아요 했는지
	
	//특정유저가 
}
 