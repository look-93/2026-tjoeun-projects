package com.moit.member.service;

import java.util.List;

import com.moit.member.dto.UserDto;

public interface MemberService {
	// 회원가입
	UserDto  signup(UserDto  dto);
	
	// 로그인용 회원조회
	UserDto findByLoginId(String loginId);
	
	// JWT 회원조회
    UserDto findByMemberId(Long memberId);
    
    // 회원정보 수정
    UserDto updateMember(Long memberId, UserDto dto);
    
    // 회원 탈퇴
    void deleteMember(Long memberId);
    
    // 소셜 회원가입
    UserDto socialSignup(UserDto dto);
    
    // 전체 회원조회
    List<UserDto> findAllMembers();

    // 중복 확인
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByMobile(String mobile);
}
