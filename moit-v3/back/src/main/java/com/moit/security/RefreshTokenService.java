package com.moit.security;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	
	private final StringRedisTemplate redisTemplate;
	
	// Refresh Token 저장
	public void saveRefreshToken(
			Long memberId,
			String refreshToken,
			long expirationMillis
			) {
		String key = "refreshToken:" + memberId;
		
		redisTemplate.opsForValue().set(
				key,
				refreshToken,
				Duration.ofMillis(expirationMillis)
				);
	}
	
	// Refresh Token 조회
	public String getRefreshToken(Long memberId) {
		
		String key = "refreshToken:" + memberId;
		
		return redisTemplate.opsForValue().get(key);
	}
	
	// Refresh Token 검증
	public boolean validateRefreshToken(
			Long memberId,
			String refreshToken
			) {
		
		String savedToken = getRefreshToken(memberId);
		
		return refreshToken.equals(savedToken);
	}
	
	// Refresh Token 삭제
	public void deleteRefreshToken(Long memberId) {
		
		String key = "refreshToken:" + memberId;
		
		redisTemplate.delete(key);
	}

}
