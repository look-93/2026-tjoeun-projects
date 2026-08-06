package com.moit.meetup.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.moit.meetup.dto.MeetupDto.MeetupRequestDto;
import com.moit.meetup.service.MeetupService;
import com.moit.member.dto.UserDto;
import com.moit.member.service.UserService;
import com.moit.qna.repository.QuestionRepository;
import com.moit.review.repository.ReviewRepository;

@SpringBootTest
@MockBean(QuestionRepository.class)
@MockBean(ReviewRepository.class)
@Transactional
public class MeetupServiceTest {
	@Autowired MeetupService meetupService;
	@Autowired UserService userService;
    @MockBean
    QuestionRepository questionRepository;

    @MockBean
    ReviewRepository reviewRepository;
	
	// 공통으로 사용할 유저를 생성해주는 헬퍼에서드
	private Long createTestUser(String loginId, String nickname) {
        UserDto dto = new UserDto();
        dto.setEmail(loginId);
        dto.setPassword("password123");
        dto.setNickname(nickname);
        dto.setMobile("01012345678");
        dto.setMemberTypeId(1);

        int result = userService.insert(dto);
        
        assertEquals(1, result);
        return dto.getMemberId().longValue();
    }
	
	// 모임생성 테스트
	@Test
	@DisplayName("★ meetupservice - CRUD : 모임생성테스트")
	void create() {
		
		Long memberId = createTestUser("test01", "테스트");
		
		MeetupRequestDto dto = new MeetupRequestDto();
		dto.setTitle("테스트");
		dto.setMaxParticipants(10);
		dto.setMinParticipants(1);
		meetupService.create(dto, memberId);
	}
		
}
