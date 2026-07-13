package com.moit.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.moit.member.dto.AuthUserDto;
import com.moit.member.dto.MyPageDto;
import com.moit.member.dto.UserDto;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails , OAuth2User{ //1.  UserDetails (security)

	private static final long serialVersionUID = 1L;

	private UserDto user;
	private AuthUserDto authDto;
	private Map<String,Object> attriubutes = new HashMap<>(); //##

	private Integer statusId;
	
		
	////////////////////////////////////// 1. 일반 로그인
	public CustomUserDetails(UserDto user, AuthUserDto authDto) {
		super();
		this.user = user;
		this.authDto = authDto;
		this.attriubutes.put("loginId", user.getLoginId());
		this.attriubutes.put("provider", user.getProvider());
		if(authDto != null){ this.statusId = authDto.getStatusId(); }
	} 
	
	@Override

	   public Collection<? extends GrantedAuthority> getAuthorities() {
			
			if(user.getMemberId() != null && user.getMemberId() == 0){
		        return List.of(
		            new SimpleGrantedAuthority("ROLE_SOCIAL")
		        );
		    }
		   
	       if (authDto == null || authDto.getTypeName() == null) {
	           return List.of(new SimpleGrantedAuthority("ROLE_MEMBER"));
	       }

	       return List.of(new SimpleGrantedAuthority(authDto.getTypeName()));
	   }	

	public Integer getAppUserId() { return user.getMemberId(); }
	public String  getEmail()     { return user.getEmail(); }
	public String  getProvider()  { return user.getProvider(); }
	public String getProfileUrl(){ return user.getProfileUrl(); }
	
	public String getNickname() { return user.getNickname(); }
	public String getTypeName() { 
		if(authDto != null) { return authDto.getTypeName(); }
		return "ROLE_MEMBER"; 
    }
	public Integer getStatusId(){ return statusId; }
    // ★ 중요
    @Override public String getPassword() { 
    	
    	if(authDto != null) { return authDto.getPassword(); }
    	
    	return "";  
    	}



    // ★ 중요
    @Override public String getUsername() {  return user.getLoginId();  }

		
	//////////////////////////////////////////////////////////////////////////// social
	// java : alt + shift + s
	public CustomUserDetails(UserDto user, Map<String, Object> attirubutes) {
		super();
		this.user = user;
		//this.authDto = new AuthUserDto();
		this.attriubutes = new HashMap<>(attirubutes != null? attirubutes : Map.of()) ;
		this.attriubutes.put("loginId", user.getLoginId());
		this.attriubutes.put("provider", user.getProvider());
	}
	
	public boolean isSocialPending(){ return user.getMemberId() == 0; }
	
	@Override public Map<String, Object> getAttributes() { return attriubutes; }
	          public void setAttributes(Map<String, Object> attributes ) { this.attriubutes = attributes; }
	
	@Override public String getName() { return user.getProviderId() ; }
	
	
}






