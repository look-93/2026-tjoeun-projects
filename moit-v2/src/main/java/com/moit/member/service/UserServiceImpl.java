package com.moit.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.member.dao.UserMapper;
import com.moit.member.dto.AuthUserDto;
import com.moit.member.dto.MyPageDto;
import com.moit.member.dto.UserDto;
import com.moit.member.dto.UserJoinDto;

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
		
		if(dto.getProfileUrl() == null) {
		    dto.setProfileUrl("/moit.png");
		}
		
		dao.insert(dto);
		dao.insertInfo(dto);
		
		return 1;
	}
	
	@Override public AuthUserDto readAuth(Map<String,Object> map) { return dao.readAuth(map); }
	
	@Override public UserDto findUser(Map<String, Object> paramMap) { return dao.findUser(paramMap); }
	
	@Override public int updateUser(UserDto dto) { return dao.updateUser(dto); }
	
	@Override public List<UserDto> select10(Map<String, Object> paramMap) { return dao.select10(paramMap); }
	
	@Override public int selectCnt(Map<String, Object> paramMap) { return dao.selectCnt(paramMap); }
	
	@Override public int deleteUser(String loginId) { return dao.deleteUser(loginId); }

	@Override public UserDto findByLoginId(UserDto dto) {  return dao.findByLoginId(dto); }

	@Override public int insertInfo(UserDto dto) { return dao.insertInfo(dto); }

	@Override public AuthUserDto readByLoginId(UserDto dto) {  return dao.readByLoginId(dto.getLoginId()); }

	@Override public UserDto findId(UserDto dto) { return dao.findId(dto); }
	
	@Override public UserDto findPasswordUser(UserDto dto) { return dao.findPasswordUser(dto); }
	
	@Override
	public boolean changePassword(UserDto dto) {
		
		int result = dao.changePassword(dto);
		
		if(result == 0) { return false; }
		
		dto.setPassword(pwencoder.encode(dto.getPassword()));
		
		dao.changePassword(dto);
		
		return true;
	}
	
//	@Transactional
//	@Override
//	public void completeSocialJoin(UserDto dto) {
//		dao.updateSocialInfo(dto);
//        dao.updateMemberInfo(dto);
//	}
	
	@Transactional
	@Override
	public void insertSocialJoin(UserDto dto) {
		dto.setPassword(pwencoder.encode(UUID.randomUUID().toString()));
		
		dto.setLoginId(dto.getProvider() + "-" + dto.getProviderId());
		
		dto.setMemberTypeId(1);
		dto.setStatusId(1);
		
		dao.insertSocial(dto);
		dao.insertSocialInfo(dto);
	}

	
	
	
	

}
