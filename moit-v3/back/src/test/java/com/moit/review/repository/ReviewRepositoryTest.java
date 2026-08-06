package com.moit.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.moit.qna.repository.QuestionNotificationRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewrepository;

    @Autowired
    private ReviewImageRepository revieimagerepository;

    @Autowired
    private ReviewLikeRepository reviewlikerepository;

    // 문제가 되는 팀원의 Repository를 MockBean으로 올려서 전체 컨텍스트 로드 에러를 방지합니다.
    @MockBean
    private QuestionNotificationRepository questionNotificationRepository;

    @Test
    @DisplayName("후기 Repository 전체 Bean 생성 테스트")
    void repositoryBeanTest() {
        assertThat(reviewrepository).isNotNull();
        assertThat(revieimagerepository).isNotNull();
        assertThat(reviewlikerepository).isNotNull();
    }
}