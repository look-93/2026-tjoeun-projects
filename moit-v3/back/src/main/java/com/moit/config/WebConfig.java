package com.moit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Value("${upload.path}") private String uploadPath; //    /upload/**
	@Value("${resource.path}") private String resourcePath; // C:/upload
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(uploadPath)
				.addResourceLocations("file:" + resourcePath + "/");
		
		// 회원 프로필 이미지
	    registry.addResourceHandler("/images/profile/**")
	            .addResourceLocations("file:uploads/profile/");
	}
	
	//Cor - 외부에서 접근가능하게 설정 (RestController) ##
    @Override
    public void addCorsMappings(CorsRegistry registry) { 
        registry.addMapping("/**") // 컨트롤러 모든경로
                .allowedOrigins("http://localhost:3000")  //프론트엔드 주소 명확히 @CrossOrigin(origins = "*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") //허용하는메서드
                .allowedHeaders("*")
                .allowCredentials(true)   //세션/쿠키연동하는 방법 / jwt -> false
                .maxAge(3600); //1*60*60 1시간 캐시에저장- 연결시간
    }	
}
