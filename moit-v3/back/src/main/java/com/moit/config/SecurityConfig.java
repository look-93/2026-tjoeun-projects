package com.moit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.moit.member.oauth2.Oauth2UserService;
import com.moit.security.CustomLoginFailureHandler;
import com.moit.security.CustomLogoutSuccessHandler;
import com.moit.security.JwtAuthenticationFilter;
import com.moit.security.SocialLoginSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   
   private final Oauth2UserService oauthUserService;
   private final SocialLoginSuccessHandler socialLoginSuccessHandler;
   private final CustomLoginFailureHandler customLoginFailureHandler;
   private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
   
   // JWT
   private final JwtAuthenticationFilter jwtAuthenticationFilter;
   
   // http 경로설정
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  { 
	   
	   // JWT 인증필터 등록
	   http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

      //1. 허용경로

      http.authorizeHttpRequests(auth -> auth.requestMatchers(
						    		    "/api/members/signup",
						    	        "/api/members/login",
						    	        "/api/members/check-loginId",
						    	        "/api/members/check-email",
						    	        "/api/members/check-nickname",
						    	        "/api/members/check-mobile",
						    	        "/api/members/refresh"
						    	        ).permitAll()
					    		  .requestMatchers(
					    				    "/user/member/join",
					    			        "/user/member/login",
					    			        "/user/checkLoginId",
					    			        "/user/checkNickname",
					    			        "/user/member/checkPassword",
					    			        "/admin/member/join",
					    			        "/meetup/list",
					    			        "/user/advertisement/click"
					    			        ).permitAll()
					    		    .requestMatchers("/api/members/**").authenticated()
                                    .requestMatchers(
                                    		"/user/member/mypage", 
                                    		"/user/member/update", 
                                    		"/user/member/delete",
                                    		"/user/advertisement/**",
                                    		"/meetup/write/**",
                                    		"/meetup/detail/**", 
                                    		"/mypage/**").authenticated()

                                    // 관리자 영역(추후 활성화 예정)
                                    //.requestMatchers("/admin/**", "/api/reports/admin/**")
                                    //.hasRole("ADMIN")
                                    // 제휴업체 광고 키워드로 ai 내용작성 
                                    .requestMatchers("/user/advertisement/aiAdvertise")
                                    .hasRole("PARTNER")
                                    .requestMatchers( "/user/member/socialInfo" )                              
                                    .hasAuthority("ROLE_SOCIAL")
                                    .anyRequest()
                                    .permitAll()         
                          );
                          //2. 로그인처리
                          http.formLogin(form -> form 
                                .loginPage("/user/member/login")
                                .loginProcessingUrl("/login")
                                //.loginProcessingUrl("/user/member/loginProc") // CustomUserDetailsService -> loadUserByUsername 호출
                                .defaultSuccessUrl("/user/main", false) // LoginSuccessHandler 동일 / 성공하면 mypage
                                .failureHandler(customLoginFailureHandler)
                                .permitAll()
                                .authenticationDetailsSource( new CustomAuthenticationDetailsSource() )
                          );
                          
                          //3. 로그아웃
                          http.logout(logout -> logout
                                .logoutUrl("/user/member/logout")
                                .logoutSuccessHandler(customLogoutSuccessHandler)
                                //.logoutSuccessUrl("/user/member/login")
                                .invalidateHttpSession(true) //session 지우기
                                .clearAuthentication(true)
                                .permitAll()                               
                          );
                          // social (oauth2)
                          http.oauth2Login(oauth2 -> oauth2
                                  .loginPage("/user/member/login")
                                  .successHandler(socialLoginSuccessHandler)
                                  .userInfoEndpoint(userinfo ->
                                          userinfo.userService(oauthUserService))
                          );
                          //4. csrf 예외처리                          

                          http.csrf(csrf -> csrf
                                .ignoringRequestMatchers(
                                		"/user/member/join", 
                                		"/user/member/update", 
                                		"/user/member/delete", 
                                		"/questions/deleteSelected", 
                                		"/api/meetup/**",
                                		"/api/members/**")

                                // Spring Security는 POST, PUT, DELETE 등의 요청에 CSRF 토큰이 있는지 검사
                                // Thymeleaf + Spring Security + <form> → CSRF 토큰이 자동으로 추가
                                // 왜추가했지..???
                              );
                          
                       // API 인증 실패 시 로그인 페이지로 redirect하지 않고 401 반환
                          http.exceptionHandling(exception -> exception
                              .defaultAuthenticationEntryPointFor(
                                  (request, response, authException) -> {
                                      response.sendError(
                                          HttpServletResponse.SC_UNAUTHORIZED,
                                          "JWT 인증이 필요합니다."
                                      );
                                  },
                                  request -> request.getRequestURI().startsWith("/api/")
                              )
                          );
      return http.build();
   }
   
   // AuthenticationManager 설정
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
   }
   
   private static class CustomAuthenticationDetailsSource implements org.springframework.security.authentication.AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
      @Override
      public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
          return new WebAuthenticationDetails(context) {      
              public String getMemberTypeId() { return context.getParameter("memberTypeId"); }
          };
      }
   }
   
}
