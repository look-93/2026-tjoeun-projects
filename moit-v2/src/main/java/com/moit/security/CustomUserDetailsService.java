package com.moit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moit.member.dao.UserMapper;
import com.moit.member.dto.AuthUserDto;
import com.moit.member.dto.UserDto;


@Service //##
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired UserMapper dao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//1. username 1@1:local  2@2:kakao
		String [] parts = username.split(":"); // {1@1 , local} = 1@1:local
		String loginId = parts[0];
		String provider = parts.length > 1? parts[1] : "local"; // local - 회원가입한 사람들만
		
		UserDto dto = new UserDto(); dto.setLoginId(loginId); dto.setProvider(provider);
		AuthUserDto authDto = dao.readByLoginId(dto); // username, password, List<AuthDto>
				
		UserDto appUserDto = dao.findByLoginId(dto); // 사용자 정보	
		
		return new CustomUserDetails(appUserDto, authDto); // 사용자 정보, 사용자 로그인 정보
	} 
}
