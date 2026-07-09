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

@Service
public class Oauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired UserMapper dao;	
	@Autowired PasswordEncoder passwordEncoder;
	
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
		
//		UserDto param = new UserDto(); 
//		param.setProvider(provider); 
//		param.setProviderId(providerId);
		
		UserDto user = dao.findByEmail(email); // 마이페이지
		
		if(user == null){

			user = new UserDto();

		    user.setLoginId(provider + "_" + providerId);

		    user.setEmail(email);

		    user.setNickname(nickname);

		    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

		    user.setProvider(provider);

		    user.setProviderId(providerId);

		    user.setProfileUrl(img);

		    user.setMemberTypeId(1);

		    user.setStatusId(5); //
		    
		    user.setMobile(null);

		    dao.insertSocial(user);
		    dao.insertSocialInfo(user);

		    //user = dao.findByEmail(email);
		}
		
//		// 이미 연동된 회원인지 확인    
//		if(user == null) { 
//			throw new OAuth2AuthenticationException("먼저 일반회원으로 가입한 후 마이페이지에서 소셜 계정을 연동해주세요.");
//		}
//		
//		// 최초 소셜 연동
//		if(user.getProvider() == null){
//
//		    user.setProvider(provider);
//		    user.setProviderId(providerId);
//
//		    dao.connectProvider(user);
//		}
//		
//		else{
//
//		    if(!provider.equals(user.getProvider())){
//		        throw new OAuth2AuthenticationException(
//		                "다른 소셜 계정으로 연동된 회원입니다.");
//		    }
//
//		    if(!providerId.equals(user.getProviderId())){
//		        throw new OAuth2AuthenticationException(
//		                "등록되지 않은 소셜 계정입니다.");
//		    }
//
//		}
			
		AuthUserDto authDto =
		        dao.readByLoginId(user.getLoginId());
		
		CustomUserDetails customUser = new CustomUserDetails(user,authDto);
		
		Map<String,Object> attributes = new HashMap<>(oAuth2User.getAttributes());
		
		attributes.put("provider", provider);
		attributes.put("providerId", providerId);
		attributes.put("email", email);
		attributes.put("nickname", nickname);
		attributes.put("profileUrl", img);
		
		customUser.setAttributes(attributes);
		
		return customUser;
	}
	
}

