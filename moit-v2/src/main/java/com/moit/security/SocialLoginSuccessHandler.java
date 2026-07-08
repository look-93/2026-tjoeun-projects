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
            throws IOException, ServletException {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        // 최초 소셜회원
        if(user.getStatusId() == 5){
            response.sendRedirect("/user/member/socialInfo");
        }else{
            response.sendRedirect("/user/main");
        }
    }
}