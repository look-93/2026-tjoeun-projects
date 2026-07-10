//package com.moit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//
//import com.moit.member.dao.UserMapper;
//import com.moit.member.dto.MyPageDto;
//import com.moit.member.dto.UserDto;
//import com.moit.member.dto.UserJoinDto;
//import com.moit.member.service.UserService;
//
//@SpringBootTest
//public class MemberApplicationTests {
//	@Autowired UserMapper dao;
//	@Autowired UserService service;
//	
//	@Test public void update_test() { // 회원정보 수정
//		UserDto dto = new UserDto();
//		
//		dto.setLoginId("second");
//		
//		dto.setNickname("second1"); dto.setPassword("2222");
//		int result = service.updateUser(dto);
//		assertEquals(1, result);
//	}
//	
//	@Disabled @Test public void findUser_test() { //중복검사
//		Map<String, Object> paramMap = new HashMap<>();
//	    //paramMap.put("loginId", "second");
//	    paramMap.put("nickname", "second");
//	    
//	    UserDto user = service.findUser(paramMap);
//
//	    assertNotNull(user);
//	    //assertEquals("second", user.getLoginId());
//	    assertEquals("second", user.getNickname());
//	}
//	
////	@Disabled @Test public void mypage_test() { //마이페이지
////		MyPageDto dto = service.findByLoginId("second");
////				
////		assertNotNull(dto);
////		assertEquals("second", dto.getLoginId());
////	}
//	
//	@Disabled @Test public void insert_test() { //회원가입
//		UserDto dto = new UserDto();
//		UserJoinDto idto = new UserJoinDto();
//		
//		dto.setLoginId("second"); dto.setMobile("01022222222"); dto.setNickname("second");
//		dto.setEmail("second@gmail.com"); dto.setPassword("2"); dto.setMemberTypeId(1);
//		dto.setStatusId(1); dto.setProfileUrl("the703.png");
//		
//		idto.setGender("M"); idto.setBirth(LocalDate.now());
//				
//		int result = service.insert(dto);
//		int result1 = service.insertInfo(idto);
//		
//		assertEquals(1, result);
//		assertEquals(1, result1);
//	}
//}
