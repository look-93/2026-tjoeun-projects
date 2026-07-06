package com.moit;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.moit.review.dao.ReviewMapper;
import com.moit.review.dto.ReviewDto;

@SpringBootTest
public class ReviewApplicationTests {
	@Autowired SqlSession sqlsession;
	@Autowired  ReviewMapper mapper;
	
	 @Test
	 public void selectUserReview_test() {
		 int meetupId = 1; 
			String sort = "latest";
		 
	 }
	 
	
	@Disabled @Test
	public void insert_test() {
		ReviewDto dto = new ReviewDto();
		
		dto.setMeetupId(4);
		dto.setMemberId(4);
		dto.setContent("다음에도 참석하고 싶습니다.");
		dto.setRating(5);
		int result = mapper.insertUserReview(dto);
		System.out.println("=========================================");
		System.out.println("======>등록 성공 행 개수: " + result);
		System.out.println("=========================================");
		
		assertEquals(1, result);
		
		
	}
	
	
	
	@Disabled @Test
	public void db_test() {
		// sqlsession.getConfiguration()을 통해 커넥션 정보를 가져옵니다.
        String url = sqlsession.getConfiguration().getEnvironment().getDataSource().toString();
        
        System.out.println("연결된 DB 정보: " + url);
        System.out.println("👍 moit 계정으로 연결 성공!");
	}
	
	
}
