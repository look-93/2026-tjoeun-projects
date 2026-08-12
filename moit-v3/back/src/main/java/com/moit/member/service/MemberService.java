package com.moit.member.service;

import com.moit.member.dto.UserDto;

public interface MemberService {
	// 회원가입
	UserDto  signup(UserDto  dto);
	
	// 로그인용 회원조회
	UserDto findByLoginId(String loginId);

    // 중복 확인
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByMobile(String mobile);
}
