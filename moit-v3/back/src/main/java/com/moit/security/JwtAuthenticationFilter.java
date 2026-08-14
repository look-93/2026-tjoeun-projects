package com.moit.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.moit.member.dto.UserDto;
import com.moit.member.service.MemberService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberService service;
	
	public JwtAuthenticationFilter(
			JwtTokenProvider jwtTokenProvider,
			MemberService memberService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.service = memberService;
	}

	@Override
	protected void doFilterInternal(
	        HttpServletRequest request,
	        HttpServletResponse response,
	        FilterChain filterChain)
	        throws ServletException, IOException {

	    // 1. Authorization 헤더 확인
	    String authorization = request.getHeader("Authorization");

	    System.out.println("===== JWT FILTER =====");
	    System.out.println("요청 URI : " + request.getRequestURI());
	    System.out.println("Authorization : " + authorization);

	    // 2. JWT가 없으면 다음 필터로
	    if (authorization == null || !authorization.startsWith("Bearer ")) {

	        System.out.println("JWT 없음");
 
	        filterChain.doFilter(request, response);
	        return;
	    }

	    // 3. Bearer 제거
	    String token = authorization.substring(7);

	    System.out.println("Token 존재 : " + !token.isEmpty());

	    // 4. JWT 검증
	    boolean valid = jwtTokenProvider.validateToken(token);

	    System.out.println("JWT 검증 결과 : " + valid);

	    if (valid) {

	    	// 5. JWT 타입 확인
	        String tokenType = jwtTokenProvider.getTokenType(token);

	        System.out.println("JWT 타입  : " + tokenType);
	        
	        // Refresh Token은 API 인증에 사용할 수 없음
	        if (!"ACCESS".equals(tokenType)) {

	            System.out.println("Access Token이 아님");

	            filterChain.doFilter(request, response);
	            return;
	        }
	        
	        // JWT에서 회원 ID 가져오기
	        Long memberId = jwtTokenProvider.getMemberId(token);

	        System.out.println("JWT memberId : " + memberId);

	        // 6. DB에서 회원 조회
	        UserDto user = service.findByMemberId(memberId);

	        System.out.println("DB 회원 조회 결과 : " + user);

	        // 7. 회원이 존재하면 인증객체 생성
	        if (user != null) {
	        	
	        	// 탈퇴 / 정지 회원 차단
	            if (user.getStatusId() == null || !user.getStatusId().equals(1L)) {

	                System.out.println("탈퇴 또는 정지 회원 - 인증 차단");

	                filterChain.doFilter(request, response);
	                return;
	            }

	            CustomUserDetails userDetails = new CustomUserDetails(user);

	            UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(
											                            userDetails,
											                            null,
											                            userDetails.getAuthorities()
											                    		);

	            authentication.setDetails( new WebAuthenticationDetailsSource() .buildDetails(request) );

	            // 8. SecurityContext에 인증정보 저장
	            SecurityContextHolder
	                    .getContext()
	                    .setAuthentication(authentication);

	            System.out.println("인증 객체 생성 완료");
	            System.out.println("현재 인증 : "
	                    + SecurityContextHolder.getContext().getAuthentication());

	        } else {

	            System.out.println("회원 조회 실패");
	        }
	    }

	    // 9. 다음 필터
	    filterChain.doFilter(request, response);
	}	
}
