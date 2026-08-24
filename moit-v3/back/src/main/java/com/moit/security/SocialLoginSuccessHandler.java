package com.moit.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler{
	
	private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
	
    @Value("${app.oauth2.redirect-url}")
    private String frontendRedirectUrl;
    
    @Value("${app.frontend-url}")
    private String frontendUrl;
    
	@Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {
		
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();		
		
		// 신규 소셜 회원
        if (user.getAppUserId() == 0L) {
        	
        	HttpSession session = request.getSession();

            // 소셜 회원정보 세션 저장
            session.setAttribute("socialUser", user.getUser());
            
            System.out.println("===== SOCIAL LOGIN =====");
            System.out.println("신규 소셜 회원");
            System.out.println("email : " + user.getEmail());
            System.out.println("provider : " + user.getProvider());
            System.out.println("providerId : " + user.getProviderId());
            System.out.println("socialUser 세션 저장 완료");
        	
            response.sendRedirect( frontendUrl + "/user/member/social-info" );
            return;
        }
        
        Long memberId = user.getAppUserId();
        String loginId = user.getUsername();

        // Access Token
        String accessToken =jwtTokenProvider.createAccessToken( memberId, loginId );

        // Refresh Token
        String refreshToken = jwtTokenProvider.createRefreshToken( memberId );

        // Redis 저장
        refreshTokenService.saveRefreshToken( memberId, refreshToken, jwtTokenProvider.getRefreshTokenExpiration() );

        // 프론트로 전달
        response.sendRedirect(
                frontendRedirectUrl
                        + "?accessToken=" + accessToken
                        + "&refreshToken=" + refreshToken
        );
    }
}