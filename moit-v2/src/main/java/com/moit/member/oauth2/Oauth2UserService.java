package com.moit.member.oauth2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.member.dao.UserMapper;
import com.moit.member.dto.AuthDto;
import com.moit.member.dto.AuthUserDto;
import com.moit.member.dto.UserDto;
import com.moit.security.CustomUserDetails;

import jakarta.servlet.http.HttpSession;

@Service
public class Oauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired UserMapper dao;	
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired HttpSession session;
	
	// alt + shift + s (override)
	@Transactional
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) 
			throws OAuth2AuthenticationException {
				
		OAuth2User oAuth2User = super.loadUser(userRequest); // attributes Map{ "key" : "value" }
		
		
		String provider = userRequest.getClientRegistration()
							         .getRegistrationId(); //kakao, naver, google
		
		UserInfoOAuth2 info = null; 
		
		     if( "google".equals(provider) ) { info = new UserGoogle(oAuth2User.getAttributes()); }
		else if( "naver".equals(provider) )  { info = new UserNaver(oAuth2User.getAttributes()); }
		else if( "kakao".equals(provider) )  { info = new UserKakao(oAuth2User.getAttributes()); }
		else {
			throw new OAuth2AuthenticationException("지원하지 않은 소셜입니다." + provider);
		}     
		
		//2. 유저정보 - email, nickname, providerId
		String email      = info.getEmail();
		String nickname   = info.getNickname();
		String providerId = info.getProviderId();
		String img = info.getImage(); //##
		
		
		UserDto user = dao.findByEmail(email); // 마이페이지
		
		if(user == null){
			
			 UserDto socialUser = new UserDto();

			    socialUser.setEmail(email);
			    socialUser.setNickname(nickname);
			    socialUser.setProvider(provider);
			    socialUser.setProviderId(providerId);
			    socialUser.setProfileUrl(img);

			    // ★ 회원가입 전 임시 저장
			    session.setAttribute("socialUser", socialUser);

			    // 로그인 객체는 임시 생성
			    user = new UserDto();
			    
			    user.setMemberId(0);
			    user.setLoginId(provider + "_" + providerId);
			    user.setEmail(email);
			    user.setNickname(nickname);
			    user.setProvider(provider);
			    user.setProviderId(providerId);
			    user.setProfileUrl(img);
		    
		}
		
		CustomUserDetails customUser;
		
		if(user.getMemberId() == 0) {

		    Map<String,Object> attributes = new HashMap<>();

		    attributes.put("provider", provider);
		    attributes.put("providerId", providerId);
		    attributes.put("email", email);
		    attributes.put("nickname", nickname);
		    attributes.put("profileUrl", img);

		    customUser = new CustomUserDetails(user, attributes);


		} else {

		    AuthUserDto authDto = dao.readByLoginId(user.getLoginId());

		    if(authDto == null) {

		        Map<String,Object> attributes = new HashMap<>();

		        attributes.put("provider", provider);
		        attributes.put("providerId", providerId);
		        attributes.put("email", email);
		        attributes.put("nickname", nickname);
		        attributes.put("profileUrl", img);

		        customUser = new CustomUserDetails(user, attributes);

		    } else {

		        customUser = new CustomUserDetails(user, authDto);

		    }
		}
		
		return customUser;
	}
	
}

