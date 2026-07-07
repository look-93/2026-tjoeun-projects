package com.moit.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moit.member.dao.UserMapper;
import com.moit.member.dto.AuthUserDto;
import com.moit.member.dto.UserDto;

@Service
public class UserServiceImpl  implements UserService{
 
	@Autowired  UserMapper dao;
	@Autowired  @Qualifier("passwordEncoder") PasswordEncoder  pwencoder;
	
	@Override
	public int insert(UserDto dto) {
		Map<String, Object> map = new HashMap<>();
		
		//아이디 중복검사
		map.put("loginId", dto.getLoginId());
		if(dao.findUser(map)!= null) { return 0; }
		
		map.clear();
		
		// 닉네임 중복검사
		map.put("nickname", dto.getNickname());
		if(dao.findUser(map) != null) { return -1; }
		
		// 비밀번호 암호화 
		dto.setPassword(pwencoder.encode(dto.getPassword()));
		
		// 회원 분류
		if(dto.getMemberTypeId()==1) { dto.setStatusId(1); } // 일반
		else if(dto.getMemberTypeId()==2) { dto.setStatusId(2); } // 제휴업체
		else if(dto.getMemberTypeId()==3) { dto.setStatusId(2); } // 관리자
		
		return dao.insert(dto);
	}
	
	@Override public AuthUserDto readAuth(Map<String,Object> map) { return dao.readAuth(map); }
	
	@Override public UserDto findMember(Map<String, Object> paramMap) { return dao.findUser(paramMap); }
	
	@Override public int updateMember(UserDto dto) { return dao.updateUser(dto); }
	
	@Override public List<UserDto> select10(Map<String, Object> paramMap) { return dao.select10(paramMap); }
	
	@Override public int selectCnt(Map<String, Object> paramMap) { return dao.selectCnt(paramMap); }
	
	@Override public int deleteMember(String loginId) { return dao.deleteUser(loginId); }

	@Override public UserDto findByLoginId(UserDto dto) {  return dao.findByLoginId(dto); }
	

}
