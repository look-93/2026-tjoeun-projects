package com.moit.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.moit.security.CustomUserDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler{
	
	@Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {
		
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();		
		
		if(user.getAppUserId() == 0) {
			response.sendRedirect("/user/member/socialInfo");
			
			return;
		}

        response.sendRedirect("/user/main");
    }
}