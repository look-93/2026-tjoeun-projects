package com.moit.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
// 1. JPA Repository 스캔 타겟을 내 패키지(com.moit.review)로만 제한하여 팀원 코드 에러 차단
@EnableJpaRepositories(basePackages = "com.moit.review")

// 2. 내장 H2 DB를 만들지 않고 메인 application.yml에 작성된 Oracle DB 설정을 그대로 사용
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

// 3. 하이버네이트가 Oracle Dialect를 인식하지 못하는 현상을 방지하기 위해 Oracle Dialect 지정
@TestPropertySource(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.OracleDialect"
})
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewrepository;

    @Autowired
    private ReviewImageRepository revieimagerepository;

    @Autowired
    private ReviewLikeRepository reviewlikerepository;

    @Test
    @DisplayName("후기 Repository 전체 Bean 생성 테스트")
    void repositoryBeanTest() {
        assertThat(reviewrepository).isNotNull();
        assertThat(revieimagerepository).isNotNull();
        assertThat(reviewlikerepository).isNotNull();
    }
}