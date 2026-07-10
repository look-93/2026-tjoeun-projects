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
		this.statusId = authDto.getStatusId();
	} 
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

	    if (authDto == null || authDto.getTypeName() == null) {
	        return List.of(new SimpleGrantedAuthority("ROLE_MEMBER"));
	    }

	    return List.of(new SimpleGrantedAuthority(authDto.getTypeName()));
	}
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		if(authDto == null || authDto.getAuthList() == null || authDto.getAuthList().isEmpty()) {
//			return List.of( new SimpleGrantedAuthority("ROLE_MEMBER"));
//		} // 권한 없으면 ROLE_MEMBER
//		
//		return authDto.getAuthList().stream()
//	            .filter( a->a.getAuth() != null  &&  !a.getAuth().isBlank() )
//	            .map(    a-> new SimpleGrantedAuthority(a.getAuth()))
//	            .collect(Collectors.toList());
//	}
//	@Override public String getPassword() { return user.getPassword(); }
//	@Override public String getUsername() { return user.getEmail() + ":" + user.getProvider(); }	

	public Integer getAppUserId() { return user.getMemberId(); }
	public String  getEmail()     { return user.getEmail(); }
	public String  getProvider()  { return user.getProvider(); }
	
	public String getNickname() { return user.getNickname(); }
	public String getTypeName() { return authDto.getTypeName(); }
	public Integer getStatusId(){ return statusId; }
    // ★ 중요
    @Override public String getPassword() {  return authDto.getPassword();  }



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
	
	@Override public Map<String, Object> getAttributes() { return attriubutes; }
	          public void setAttributes(Map<String, Object> attributes ) { this.attriubutes = attributes; }
	
	@Override public String getName() { return user.getProviderId() ; }
	
	
}






