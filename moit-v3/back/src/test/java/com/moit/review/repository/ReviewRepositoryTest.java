package com.moit.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
