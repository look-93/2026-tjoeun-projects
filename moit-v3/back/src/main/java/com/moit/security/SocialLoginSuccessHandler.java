package com.moit.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.moit.member.service.LoginHistoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler{
	
	private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final LoginHistoryService loginHistoryService;
	
	@Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {
		
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();	
		
		HttpSession session = request.getSession();
		
		String deviceId = (String) session.getAttribute("deviceId");
		
		// 신규 소셜 회원
        if (user.getAppUserId() == 0L) {

            // 소셜 회원정보 세션 저장
            session.setAttribute("socialUser", user.getUser());
            
            System.out.println("===== SOCIAL LOGIN =====");
            System.out.println("신규 소셜 회원");
            System.out.println("email : " + user.getEmail());
            System.out.println("provider : " + user.getProvider());
            System.out.println("providerId : " + user.getProviderId());
            System.out.println("socialUser 세션 저장 완료");
        	
            response.sendRedirect( "http://localhost:3000/user/member/social-info" );
            return;
        }
        
        // 기존 소셜 회원
        Long memberId = user.getAppUserId();
        String loginId = user.getUsername();
        
        // 로그인 정보
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        
        // 로그인 기록 저장
        loginHistoryService.saveLoginHistory(
                memberId,
                ipAddress,
                userAgent,
                "SOCIAL"
        );
              
        // Access Token
        String accessToken =jwtTokenProvider.createAccessToken( memberId, loginId ,deviceId);

        // Refresh Token
        String refreshToken = jwtTokenProvider.createRefreshToken( memberId );

        // Redis 저장
        refreshTokenService.saveRefreshToken( memberId, deviceId, refreshToken, jwtTokenProvider.getRefreshTokenExpiration() );

        // 프론트로 전달
        response.sendRedirect(
                "http://localhost:3000/oauth2/callback"
                        + "?accessToken=" + accessToken
                        + "&refreshToken=" + refreshToken
        );
    }
}