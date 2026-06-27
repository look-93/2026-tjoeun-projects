package com.moit.service;

import com.moit.dto.AuthDto;
import com.moit.dto.AuthUserDto;
import com.moit.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
	
	public int      	 insert(UserDto dto);  // 회원가입 + 권한추가
	public     AuthUserDto    readAuth( Map<String,Object> map); // 로그인 인증 정보 조회
	public UserDto  findMember(Map<String,Object> paramMap); // 아이디 중복검사, 닉네임 중복검사
	public int updateMember(UserDto dto); // 회원정보 수정 (닉네임,비밀번호,프로필,탈퇴,관리자승인/권한변경)
	public List<UserDto> select10(Map<String,Object> paramMap); // 회원리스트 페이징 조회
	public int selectCnt(Map<String,Object> paramMap); // 조건별 전체 회원 수 조회
	public int deleteMember(String loginId); // 관리자기능 - 회원 삭제
	
	/* security login */
	public UserDto findByLoginId(String loginId);
	
}

