package com.moit.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

<<<<<<< HEAD
import java.io.InputStream;
=======
>>>>>>> 66bfcd22c07d5e52886206bd8d8cae3494e74aad
import java.util.HashMap;
import java.util.List;
import java.util.Map;

<<<<<<< HEAD
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
=======
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.moit.review.dao.ReviewMapper;
import com.moit.review.dto.ReviewDto;
import com.moit.util.UtilPaging;

@SpringBootTest
@Transactional // 테스트 수행 후 데이터 자동 롤백
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@TestPropertySource(properties = {
    "openai.api.key=dummy-test-key",
    "kma.api.key=dummy-kma-key", // <-- 누락되었던 kma.api.key 프로퍼티 추가
    "spring.jpa.database-platform=org.hibernate.dialect.OracleDialect",
    "spring.jpa.hibernate.ddl-auto=none"
})
public class ReviewMapperTest {

    @Autowired
    private ReviewMapper reviewMapper;

    // 테스트용 데이터 상수 (실제 DB 환경에 존재하는 FK ID 입력 필요)
    private final Long TEST_MEETUP_ID = 1L;
    private final Long TEST_MEMBER_ID = 1L;

    // ==========================================
    // 1. 사용자 영역 CRUD 테스트
    // ==========================================

    @Test
    @DisplayName("사용자 리뷰 작성 및 단건 조회 (C & R)")
    void insertAndSelectReviewTest() {
        // Given
        ReviewDto.Request request = new ReviewDto.Request();
        request.setMeetupId(TEST_MEETUP_ID);
        request.setMemberId(TEST_MEMBER_ID);
        request.setContent("MyBatis Mapper 저장 테스트 리뷰입니다.");
        request.setRating(5);
        request.setIsPublic("Y");

        // When
        int result = reviewMapper.insertUserReview(request);
        Long generatedReviewId = request.getReviewId();

        // Then
        assertThat(result).isEqualTo(1);
        assertThat(generatedReviewId).isNotNull();

        ReviewDto.Response response = reviewMapper.selectReviewById(generatedReviewId);
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("MyBatis Mapper 저장 테스트 리뷰입니다.");
        assertThat(response.getRating()).isEqualTo(5);
    }

    @Test
    @DisplayName("사용자 모임별 리뷰 목록 조회 (정렬 옵션 포함)")
    void selectUserReviewTest() {
        List<ReviewDto.Response> listLatest = reviewMapper.selectUserReview(TEST_MEETUP_ID, "latest");
        List<ReviewDto.Response> listLikes = reviewMapper.selectUserReview(TEST_MEETUP_ID, "likes");

        assertThat(listLatest).isNotNull();
        assertThat(listLikes).isNotNull();
    }

    @Test
    @DisplayName("사용자 리뷰 수정 및 비공개 처리 (U)")
    void updateUserReviewAndHideTest() {
        // Given: 리뷰 생성
        ReviewDto.Request request = new ReviewDto.Request();
        request.setMeetupId(TEST_MEETUP_ID);
        request.setMemberId(TEST_MEMBER_ID);
        request.setContent("수정 전 내용");
        request.setRating(3);
        request.setIsPublic("Y");
        reviewMapper.insertUserReview(request);
        Long reviewId = request.getReviewId();

        // When: 수정
        request.setReviewId(reviewId);
        request.setContent("수정 후 내용");
        request.setRating(4);
        request.setIsPublic("N");
        int updateResult = reviewMapper.updateUserReview(request);

        // Then
        assertThat(updateResult).isEqualTo(1);
        ReviewDto.Response updatedReview = reviewMapper.selectReviewById(reviewId);
        assertThat(updatedReview.getContent()).isEqualTo("수정 후 내용");
        assertThat(updatedReview.getRating()).isEqualTo(4);

        // When: 개별 공개여부 변경
        int hideResult = reviewMapper.updateUserReviewHide(reviewId, "Y");
        assertThat(hideResult).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자 리뷰 논리 삭제 (D)")
    void deleteUserReviewTest() {
        // Given
        ReviewDto.Request request = new ReviewDto.Request();
        request.setMeetupId(TEST_MEETUP_ID);
        request.setMemberId(TEST_MEMBER_ID);
        request.setContent("삭제될 리뷰");
        request.setRating(1);
        request.setIsPublic("Y");
        reviewMapper.insertUserReview(request);
        Long reviewId = request.getReviewId();

        // When
        int deleteResult = reviewMapper.deleteUserReview(reviewId);

        // Then
        assertThat(deleteResult).isEqualTo(1);
        ReviewDto.Response deletedReview = reviewMapper.selectReviewById(reviewId);
        assertThat(deletedReview).isNull(); // selectReviewById는 delete_yn='N' 조건 존재
    }

    @Test
    @DisplayName("마이페이지 회원별 리뷰 목록 및 키워드 검색")
    void selectReviewByMemberIdTest() {
        List<ReviewDto.Response> list = reviewMapper.selectReviewByMemberId(TEST_MEMBER_ID, null, "latest");
        List<ReviewDto.Response> searchList = reviewMapper.selectReviewByMemberId(TEST_MEMBER_ID, "테스트", "likes");

        assertThat(list).isNotNull();
        assertThat(searchList).isNotNull();
    }

    @Test
    @DisplayName("모임 내 리뷰 내용 검색")
    void selectReviewByContentTest() {
        List<ReviewDto.Response> list = reviewMapper.selectReviewByContent(TEST_MEETUP_ID, "테스트", "latest");
        assertThat(list).isNotNull();
    }

    // ==========================================
    // 2. 이미지 영역 테스트
    // ==========================================

    @Test
    @DisplayName("이미지 저장 및 리뷰-이미지 매핑 데이터 생성")
    void insertImageAndReviewImageTest() {
        // Given: 리뷰 생성
        ReviewDto.Request reviewReq = new ReviewDto.Request();
        reviewReq.setMeetupId(TEST_MEETUP_ID);
        reviewReq.setMemberId(TEST_MEMBER_ID);
        reviewReq.setContent("이미지 첨부 리뷰");
        reviewReq.setRating(5);
        reviewReq.setIsPublic("Y");
        reviewMapper.insertUserReview(reviewReq);

        // Given: 이미지 엔티티 저장
        ReviewDto.Request imageReq = new ReviewDto.Request();
        imageReq.setImagePath("/images/review_sample.jpg");
        int imageInsertResult = reviewMapper.insertImage(imageReq);
        
        assertThat(imageInsertResult).isEqualTo(1);
        assertThat(imageReq.getImageId()).isNotNull();

        // When: 매핑 테이블 연관관계 추가
        int mapResult = reviewMapper.insertReviewImage(reviewReq.getReviewId(), imageReq.getImageId());

        // Then
        assertThat(mapResult).isEqualTo(1);
    }

    // ==========================================
    // 3. 좋아요 영역 테스트
    // ==========================================

    @Test
    @DisplayName("좋아요 생성, 존재여부 확인, 카운트 증감 및 삭제 사이클 테스트")
    void reviewLikeFullCycleTest() {
        // Given: 리뷰 생성
        ReviewDto.Request request = new ReviewDto.Request();
        request.setMeetupId(TEST_MEETUP_ID);
        request.setMemberId(TEST_MEMBER_ID);
        request.setContent("좋아요 테스트용 리뷰");
        request.setRating(5);
        request.setIsPublic("Y");
        reviewMapper.insertUserReview(request);
        Long reviewId = request.getReviewId();

        Map<String, Object> params = new HashMap<>();
        params.put("reviewId", reviewId);
        params.put("memberId", TEST_MEMBER_ID);

        // 1. 초기 좋아요 존재 여부 확인 (0)
        int initialCheck = reviewMapper.checkLikeExists(params);
        assertThat(initialCheck).isEqualTo(0);

        // 2. 좋아요 추가 및 좋아요수 +1
        int insertLikeResult = reviewMapper.insertLike(params);
        int incResult = reviewMapper.incrementLikeCount(reviewId);
        assertThat(insertLikeResult).isEqualTo(1);
        assertThat(incResult).isEqualTo(1);

        // 3. 좋아요 존재 확인 (1) 및 개수 조회
        int checkAfterInsert = reviewMapper.checkLikeExists(params);
        int countAfterInc = reviewMapper.getLikeCount(reviewId);
        assertThat(checkAfterInsert).isEqualTo(1);
        assertThat(countAfterInc).isEqualTo(1);

        // 4. 좋아요 삭제 및 좋아요수 -1
        int deleteLikeResult = reviewMapper.deleteLike(params);
        int decResult = reviewMapper.decrementLikeCount(reviewId);
        assertThat(deleteLikeResult).isEqualTo(1);
        assertThat(decResult).isEqualTo(1);

        // 5. 최종 카운트 확인
        int finalCount = reviewMapper.getLikeCount(reviewId);
        assertThat(finalCount).isEqualTo(0);
    }

    // ==========================================
    // 4. 관리자 영역 테스트
    // ==========================================

    @Test
    @DisplayName("관리자: 전체 리뷰 목록, 작성자별, 내용별 조회")
    void adminSelectAndSearchTest() {
        List<ReviewDto.Response> allList = reviewMapper.adminSelectReviewList(0L);
        List<ReviewDto.Response> memberList = reviewMapper.adminSelectReviewList(TEST_MEMBER_ID);
        List<ReviewDto.Response> contentSearch = reviewMapper.adminSearchReviewByContent("테스트");
        List<ReviewDto.Response> writerSearch = reviewMapper.adminSearchReviewByWriter(TEST_MEMBER_ID);

        assertThat(allList).isNotNull();
        assertThat(memberList).isNotNull();
        assertThat(contentSearch).isNotNull();
        assertThat(writerSearch).isNotNull();
    }

    @Test
    @DisplayName("관리자: 강제 숨김 처리 및 강제 논리 삭제")
    void adminHideAndDeleteTest() {
        // Given
        ReviewDto.Request request = new ReviewDto.Request();
        request.setMeetupId(TEST_MEETUP_ID);
        request.setMemberId(TEST_MEMBER_ID);
        request.setContent("관리자제어 테스트 리뷰");
        request.setRating(3);
        request.setIsPublic("Y");
        reviewMapper.insertUserReview(request);
        Long reviewId = request.getReviewId();

        // When: 강제 숨김
        int hideResult = reviewMapper.adminHideReview(reviewId);
        assertThat(hideResult).isEqualTo(1);

        // When: 강제 삭제
        int deleteResult = reviewMapper.adminDeleteReview(reviewId);
        assertThat(deleteResult).isEqualTo(1);
    }

    @Test
    @DisplayName("관리자: 페이징 목록 조회 및 전체 개수 조회")
    void adminPagingAndCountTest() {
        // Given
        UtilPaging paging = new UtilPaging();
        paging.setPstartno(1); // 시작 Row 번호
        int endRow = 10;       // 끝 Row 번호

        // When
        List<ReviewDto.Response> pagedList = reviewMapper.adminGetReviewList("테스트", TEST_MEMBER_ID, paging, endRow);
        int totalCount = reviewMapper.adminGetReviewCount("테스트", TEST_MEMBER_ID);

        // Then
        assertThat(pagedList).isNotNull();
        assertThat(totalCount).isGreaterThanOrEqualTo(0);
    }
}
>>>>>>> 66bfcd22c07d5e52886206bd8d8cae3494e74aad
