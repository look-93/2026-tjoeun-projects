package com.moit.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.moit.dao.UserMapper;
import com.moit.dto.AuthUserDto;
import com.moit.service.UserService;
 
public class CustomUserDetailsService   implements UserDetailsService{

	@Autowired  UserService service;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String,Object> map = new HashMap<>();
		
		map.put("loginId", username);
		
		AuthUserDto  dto = service.readAuth(map);  // loginId, bpass, auth(s)
		
		return  dto == null?  null : new CustomUser(dto);
	}

}
