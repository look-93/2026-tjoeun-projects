package com.moit.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {
	@Autowired MemberInfoRepository memberInfoRepository;
	@Autowired MemberRepository memberRepository;
	@Autowired MemberStatusRepository memberStatusRepository;
	@Autowired MemberTypeRepository memberTypeRepository;
	@Autowired ReportStatusRepository reportStatusRepository;
	
	@Test
	@DisplayName("Member Repository 전체 Bean 생성 테스트")
	void repositoryBeanTest() {
		assertThat(memberInfoRepository).isNotNull();
		assertThat(memberRepository).isNotNull();
		assertThat(memberStatusRepository).isNotNull();
		assertThat(memberTypeRepository).isNotNull();
		assertThat(reportStatusRepository).isNotNull();	
	}
	
	/*
     * 테스트 공통 데이터
     *
     * 회원 1명
     * 회원유형 1개
     * 관심사 1개
     *
     * 각 테스트 실행 전 초기 데이터 생성
     */

    private Members member;

    private Interest interest;

    private MemberType memberType;



    @BeforeEach
    void setup(){


        //--------------------------------
        // 회원 유형 생성
        //--------------------------------

        memberType = new MemberType();

        memberType.setMemberTypeId(1L);
        memberType.setTypeName("ROLE_MEMBER");


        memberTypeRepository.save(memberType);



        //--------------------------------
        // 회원 생성
        //--------------------------------

        member = new Members();

        member.setLoginId( "test_" + UUID.randomUUID() );
        member.setNickname( "nickname_" + UUID.randomUUID() );
        member.setEmail( UUID.randomUUID()+"@test.com" );
        member.setPassword("1234");
        member.setMemberType(memberType);

        memberRepository.save(member);


        //--------------------------------
        // 관심사 생성
        //--------------------------------

        interest = new Interest();

        interest.setInterestId(1L);
        interest.setInterestName("운동");

        interestRepository.save(interest);
    }

    /*
     * =========================================
     * MemberRepository 테스트
     * =========================================
     */

    @Test
    @DisplayName("■ MemberRepository - 회원 PK 조회")
    void findMemberByIdTest(){

        Optional<Members> result = memberRepository.findById( member.getMemberId() );

        assertThat(result) .isPresent();
        assertThat( result.get().getLoginId() ) .isEqualTo( member.getLoginId() );
    }

    @Test
    @DisplayName("■ MemberRepository - 로그인 아이디 조회")
    void findByLoginIdTest(){

        Optional<Members> result = memberRepository .findByLoginId( member.getLoginId() );

        assertThat(result) .isPresent();
        assertThat( result.get() .getNickname() ) .isEqualTo( member.getNickname() );
    }

    /*
     * =========================================
     * MemberInfo Repository 테스트
     *
     * 회원 상세정보
     * Members 1 : 1 MemberInfo
     * =========================================
     */

    @Test
    @DisplayName("■ MemberInfoRepository - 회원 상세정보 등록")
    void memberInfoTest(){

        MemberInfo info = new MemberInfo();

        info.setMember(member);
        info.setGender("M");
        info.setBirth( LocalDate.of(2000,1,1) );
        info.setPoint(0);
        info.setTrustScore(100);

        memberInfoRepository.save(info);

        Optional<MemberInfo> result = memberInfoRepository .findById( member.getMemberId() );

        assertThat(result) .isPresent();
        assertThat( result.get() .getGender() ) .isEqualTo("M");
    }

    /*
     * =========================================
     * Interest Repository 테스트
     * =========================================
     */

    @Test
    @DisplayName("■ InterestRepository - 관심사 조회")
    void interestRepositoryTest(){

        List<Interest> list = interestRepository.findAll();

        assertThat(list) .isNotEmpty();
        assertThat( list.get(0) .getInterestName() ) .isEqualTo("운동");
    }

    /*
     * =========================================
     * MemberInterest Repository 테스트
     *
     * 회원 <-> 관심사 연결 테이블
     * =========================================
     */

    @Test
    @DisplayName("■ MemberInterestRepository - 회원 관심사 등록")
    void memberInterestInsertTest(){

        MemberInterest memberInterest = new MemberInterest();

        memberInterest.setMember(member);
        memberInterest.setInterest(interest);

        memberInterestRepository.save(memberInterest);

        List<MemberInterest> list = memberInterestRepository .findByMemberMemberId( member.getMemberId() );

        assertThat(list) .hasSize(1);
        assertThat( list.get(0) .getInterest() .getInterestName() ) .isEqualTo("운동");

    }

    @Test
    @DisplayName("■ MemberInterestRepository - 회원 관심사 삭제")
    void memberInterestDeleteTest(){

        MemberInterest memberInterest = new MemberInterest();

        memberInterest.setMember(member);
        memberInterest.setInterest(interest);

        memberInterestRepository.save(memberInterest);


        //--------------------------------
        // 관심사 전체 삭제
        //--------------------------------

        memberInterestRepository .deleteByMemberMemberId( member.getMemberId() );

        List<MemberInterest> list = memberInterestRepository .findByMemberMemberId( member.getMemberId() );

        assertThat(list) .isEmpty();

    }


    /*
     * =========================================
     * PointHistory Repository 테스트
     *
     * 포인트 사용/적립 내역
     * =========================================
     */

    @Test
    @DisplayName("■ PointHistoryRepository - 포인트 내역 조회")
    void pointHistoryTest(){

        PointHistory history = new PointHistory();

        history.setMember(member);
        history.setPointPm(100);
        history.setPointType("SAVE");
        history.setPointReason( "회원가입 이벤트" );

        pointHistoryRepository.save(history);


        List<PointHistory> list = pointHistoryRepository .findByMemberMemberIdOrderByCreatedAtDesc( member.getMemberId() );

        assertThat(list) .isNotEmpty();
        assertThat( list.get(0) .getPointReason() ) .isEqualTo( "회원가입 이벤트" );

    }

}
