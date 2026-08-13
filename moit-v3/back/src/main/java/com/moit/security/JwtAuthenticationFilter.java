package com.moit.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtTokenProvider jwtTokenProvider;
	
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		//1. Authorization 헤더에서 JWT 가져오기
		String authorization = request.getHeader("Authorization");
		
		//2. JWT가 없으면 다음 필터로 이동
		if(authorization == null || !authorization.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//3. "Bearer " 제거하고 실제토큰만 가져오기
		String token = authorization.substring(7);
		
		//4. JWT 검증
		if(jwtTokenProvider.validateToken(token)) {
			
			//5. JWT에서 회원 ID 가져오기
			Long memberId = jwtTokenProvider.getMemberId(token);
			
			//6. Spring Security 인증객체 생성  (추후 수정 예정)
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(memberId,null,null);
			
			//7. 요청 정보 인증객체에 추가
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			//8. Security Context에 인증 정보 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		//9. 다음 필터로 이동
		filterChain.doFilter(request, response);
		
	}
	
	
}
