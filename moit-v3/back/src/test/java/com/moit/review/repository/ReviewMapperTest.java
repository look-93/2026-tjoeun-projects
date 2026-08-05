package com.moit.review.repository;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.meetup.entity.Meetup;
import com.moit.member.entity.Member;
import com.moit.review.entity.Review;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ReviewMapperTest {
	
	@Autowired
	private ReviewRepository reviewrepository;
	
	@Autowired
	private ReviewImageRepository reviewimagerepository;
	
	@Autowired
	private ReviewLikeRepository reviewlikerepository;
	
	@Autowired 
	private EntityManager entityManager;
	
	
	private Member member;
    private Meetup meetup;
    private Review review;
	
	@BeforeEach
	void test1() {
		
		// 1. 회원 생성 및 저장 (기초 데이터)
        member = new Member();
        // member.set... 필요한 필드 세팅
        entityManager.persist(member);
        
//        meetup = new Meetup();
//        entityManager.persist(meetup);
        
        
        review = new Review();
        
        
        
        
	}
	
	
	
}
