package com.moit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moit.member.dao.UserMapper;
import com.moit.member.dto.AuthUserDto;
import com.moit.member.dto.MyPageDto;
import com.moit.member.dto.UserDto;


@Service //##
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired UserMapper dao;	
	
	@Override
	public UserDetails loadUserByUsername(String username)
	        throws UsernameNotFoundException {


	    AuthUserDto authDto = dao.readByLoginId(username);

	    if(authDto == null){
	        throw new UsernameNotFoundException( "사용자를 찾을 수 없습니다 : " + username );
	    }
	   
	    if(authDto.getStatusId() == 2) {
	        throw new BadCredentialsException("WAIT");
	    }
	    
	    UserDto user = new UserDto();

	    user.setLoginId( username );
	    
	    UserDto dto = dao.findByLoginId(user);
	    
	    if (dto == null) {
            throw new UsernameNotFoundException( "회원정보를 찾을 수 없습니다 : " + username);
        }	  
	    	    
	    return new CustomUserDetails( dto, authDto );

	}
}
