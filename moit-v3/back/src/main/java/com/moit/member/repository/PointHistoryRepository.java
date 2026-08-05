package com.moit.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.member.entity.PointHistory;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>{
	
	// 회원 포인트 사용내역 조회
	List<PointHistory> findByMemberMemberIdOrderByCreatedAtDesc(Long memberId);
}
