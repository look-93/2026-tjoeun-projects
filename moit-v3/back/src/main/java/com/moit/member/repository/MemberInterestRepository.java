package com.moit.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.member.entity.MemberInterest;

@Repository
public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long>{
	
	// 회원 관심사 목록 조회
	List<MemberInterest> findByMemberId(Long memberId);
	
	// 회원 관심사 전체 삭제
	void deleteByMemberId(Long memberId);
}
