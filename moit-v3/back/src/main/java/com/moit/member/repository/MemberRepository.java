package com.moit.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
	
	// 로그인 아이디로 조회
	Optional<Member> findByLoginId(String loginId);
	
	Optional<Member> findByEmail(String email);
	
	Optional<Member> findByNickname(String nickname);
	
	// 아이디 중복검사
	boolean  existsByLoginId(String loginId);
	
	// 이메일 중복검사
	boolean  existsByEmail(String email);
	
	// 닉네임 중복검사
	boolean  existsByNickname(String nickname);
	
	boolean existsByMobile(String mobile);
}
