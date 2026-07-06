package com.moit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	// http 경로설정
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  { 
		
		//1. 허용경로
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/user/join", "/user/login", "/user/checkLoginId" , "/users/checkNickname" , "/api/**").permitAll()
											   .requestMatchers("/user/mypage", "/user/update", "/user/delete").authenticated()
											   .anyRequest().permitAll()
				
								  )//2. 로그인처리
								  .formLogin(form -> form 
										  .loginPage("/user/login")
										  .loginProcessingUrl("/user/loginProc") // CustomUserDetailsService -> loadUserByUsername 호출
										  .defaultSuccessUrl("/user/mypage", true) // LoginSuccessHandler 동일 / 성공하면 mypage
										  .failureUrl("/user/fail")
										  .permitAll()
								  )//3. 로그아웃
								  .logout(logout -> logout
										  .logoutUrl("/user/logout")
										  .logoutSuccessUrl("/user/login")
										  .invalidateHttpSession(true) //session 지우기
										  .clearAuthentication(true)
										  .permitAll()
								  )//4. csrf 예외처리								  
								  .csrf(csrf -> csrf
										  .ignoringRequestMatchers("/user/join", "/user/update", "/user/delete")
										  // Spring Security는 POST, PUT, DELETE 등의 요청에 CSRF 토큰이 있는지 검사
										  // Thymeleaf + Spring Security + <form> → CSRF 토큰이 자동으로 추가
										  // 왜추가했지..???
									   );
		return http.build();
	}
	
	// AuthenticationManager 설정
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
