package com.moit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.review.dao.ReviewMapper;
import com.moit.review.dto.ReviewDto;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ReviewApplicationTests {
	@Autowired SqlSession sqlsession;
	@Autowired  ReviewMapper mapper;
	
	@Test
	public void delete_test() {
		ReviewDto dto = new ReviewDto();
		
		dto.setReviewId(1);
		int result = mapper.deleteUserReview(dto);
		System.out.println("=========================================");
		System.out.println("======>리뷰 논리삭제 성공 행 개수: " + result);
		System.out.println("=========================================");
		
		assertEquals(1, result);
		
	}
	
	@Disabled @Test
	public void update_test() {
		ReviewDto dto = new ReviewDto();
		
		dto.setReviewId(1);
		dto.setContent("내용 변경");
		dto.setRating(4);
		int result = mapper.updateUserReview(dto);
		System.out.println("=========================================");
		System.out.println("======>리뷰 수정 성공 행 개수: " + result);
		System.out.println("=========================================");
		
		assertEquals(1, result);
		
	}
	
	@Disabled @Test
	 public void selectUserReview_test() {
		 int meetupId = 1; 
			String sort = "latest";
			List<ReviewDto> list=mapper.selectUserReview(meetupId, sort);
			
			System.out.println("=========================================");
			System.out.println("======조회된 리뷰 개수: " + list.size());
			assertNotNull(list);
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
        System.out.println("연결 성공!");
	}
	
	
}
