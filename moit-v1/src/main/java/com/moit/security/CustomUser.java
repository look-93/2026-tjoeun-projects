package com.moit.security;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.moit.dto.AuthUserDto;

import lombok.Getter;

@Getter 
public class CustomUser extends User{  
	private static final long serialVersionUID = 1L; 
 
	AuthUserDto  dto;
	

	public CustomUser(String username, 
				      String password, 
				      Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities); 
	}


    public CustomUser(AuthUserDto  dto) {
    	super(  dto.getLoginId() , dto.getPassword() 
    						   , Collections.singletonList(new SimpleGrantedAuthority(dto.getTypeName()))
    						        );
    	this.dto = dto;
    } 
    
}




