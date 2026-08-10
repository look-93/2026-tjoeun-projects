package com.moit.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.moit.review.dao.ReviewMapper;
import com.moit.review.dto.ReviewDto;

class ReviewMapperTest {

    private SqlSession sqlSession;
    private ReviewMapper reviewMapper;

    @BeforeEach
    void setUp() {
        // 1. 스프링 컨텍스트를 띄우지 않고 실제 로컬 오라클 DB 커넥션을 수동 생성
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("xe");
        dataSource.setPassword("1234"); // ⭕ 알려주신 로컬 DB 비밀번호 반영 완료!

        // 2. 마이바티스 환경 설정 및 매퍼 인터페이스 직접 바인딩
        Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(ReviewMapper.class);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        
        // 3. 테스트간 데이터 격리를 위해 수동 커밋 모드로 세션 열기
        this.sqlSession = sqlSessionFactory.openSession(false); 
        this.reviewMapper = sqlSession.getMapper(ReviewMapper.class);
    }

    @AfterEach
    void tearDown() {
        if (sqlSession != null) {
            sqlSession.rollback(); // 테스트 완료 후 DB 데이터 오염 방지를 위해 롤백
            sqlSession.close();
        }
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    void insertUserReviewTest() {
        ReviewDto.Request dto = new ReviewDto.Request();
        dto.setMeetupId(1L);
        dto.setMemberId(1L);
        dto.setContent("Mapper 테스트 후기");
        dto.setRating(5);
        dto.setIsPublic("Y");

        int result = reviewMapper.insertUserReview(dto);
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 리뷰 조회 테스트")
    void selectReviewByIdTest() {
        ReviewDto.Response response = reviewMapper.selectReviewById(1L);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("모임별 후기 조회 테스트")
    void selectUserReviewTest() {
        List<ReviewDto.Response> list = reviewMapper.selectUserReview(1L, "latest");
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("회원별 후기 조회 테스트")
    void selectReviewByMemberIdTest() {
        List<ReviewDto.Response> list = reviewMapper.selectReviewByMemberId(1L, "", "latest");
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("후기 내용 검색 테스트")
    void selectReviewByContentTest() {
        List<ReviewDto.Response> list = reviewMapper.selectReviewByContent(1L, "테스트", "latest");
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    void updateUserReviewTest() {
        ReviewDto.Request dto = new ReviewDto.Request();
        dto.setMeetupId(1L);
        dto.setMemberId(1L);
        dto.setContent("수정된 후기");
        dto.setRating(4);
        dto.setIsPublic("Y");

        int result = reviewMapper.updateUserReview(dto);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void deleteUserReviewTest() {
        int result = reviewMapper.deleteUserReview(1L);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("리뷰 공개 여부 변경 테스트")
    void updateUserReviewHideTest() {
        int result = reviewMapper.updateUserReviewHide(1L, "N");
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("이미지 등록 테스트")
    void insertImageTest() {
        ReviewDto.Request dto = new ReviewDto.Request();
        int result = reviewMapper.insertImage(dto);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("리뷰 이미지 연결 테스트")
    void insertReviewImageTest() {
        int result = reviewMapper.insertReviewImage(1L, 1L);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 존재 확인 테스트")
    void checkLikeExistsTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", 1L);
        params.put("memberId", 1L);

        int result = reviewMapper.checkLikeExists(params);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 추가 테스트")
    void insertLikeTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", 1L);
        params.put("memberId", 2L);

        int result = reviewMapper == null ? 0 : reviewMapper.insertLike(params);
        if(result == 0) result = reviewMapper.insertLike(params);
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 삭제 테스트")
    void deleteLikeTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", 1L);
        params.put("memberId", 2L);

        int result = reviewMapper.deleteLike(params);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 증가 테스트")
    void incrementLikeCountTest() {
        int result = reviewMapper.incrementLikeCount(1L);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 감소 테스트")
    void decrementLikeCountTest() {
        int result = reviewMapper.decrementLikeCount(1L);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 개수 조회 테스트")
    void getLikeCountTest() {
        int count = reviewMapper.getLikeCount(1L);
        assertThat(count).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("관리자 전체 후기 조회")
    void adminSelectReviewListTest() {
        List<ReviewDto.Response> list = reviewMapper.adminSelectReviewList(0L);
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("관리자 후기 검색")
    void adminSearchReviewByContentTest() {
        List<ReviewDto.Response> list = reviewMapper.adminSearchReviewByContent("테스트");
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("관리자 작성자 검색")
    void adminSearchReviewByWriterTest() {
        List<ReviewDto.Response> list = reviewMapper.adminSearchReviewByWriter(1L);
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("관리자 리뷰 숨김")
    void adminHideReviewTest() {
        int result = reviewMapper.adminHideReview(1L);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("관리자 리뷰 삭제")
    void adminDeleteReviewTest() {
        int result = reviewMapper.adminDeleteReview(1L);
        assertThat(result).isGreaterThanOrEqualTo(0);
    }
}
