//package moit;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.sql.DataSource;
//
//import org.apache.ibatis.session.SqlSession;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.moit.dao.AnswerMapper;
//import com.moit.dao.QuestionMapper;
//import com.moit.dto.AnswerDto;
//import com.moit.dto.QuestionDto;
//import com.moit.dao.ReportsMapper;
//import com.moit.dto.ReportsDto;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={
//        "classpath:config/root-context.xml",
//        "classpath:config/security-context.xml" 
//})
//public class UserTest {
//
//	@Autowired   ApplicationContext context;
//	@Autowired   DataSource         ds;
//	@Autowired   SqlSession         sqlSession;
//	@Autowired   QuestionMapper     question;
//	@Autowired   AnswerMapper		answer;
//	
//	
//	@Ignore @Test // 질문별 답변 조회
//	public void findByQuestionId() {
//	    AnswerDto dto = answer.findByQuestionId(11);
//	    System.out.println(dto);
//	}
//	
//	@Ignore @Test // 답변 삭제 (논리 삭제)
//	public void deleteAnswer() {
//	    answer.deleteAnswer(12);
//	    System.out.println("답변 삭제 완료");
//	}
//	
//	@Ignore @Test // 답변 수정
//	 public void updateAnswer() {
//	     AnswerDto dto = new AnswerDto();
//	@Autowired ReportsMapper	mapper;
//	
//	@Ignore //@Test
//	public void testAdminUpdateStatus() { // 신고 상태 변경
//	    ReportsDto dto = new ReportsDto();
//	    dto.setReportId(8);        // 테스트할 reportId
//	    dto.setStatus("APPROVED"); // 바꿀 상태
//	    int result = mapper.updateAdmin(dto);
//	    System.out.println("updateAdmin result = " + result);
//	}
//
//	@Ignore //@Test
//	public void testAdminDelete() { // 신고 강제 삭제
//	    int result = mapper.deleteAdmin(8); // reportId = 8 삭제
//	    System.out.println("deleteAdmin result = " + result);
//	}
//
//	@Ignore //@Test
//	public void testAdminSelectReports() { // 관리자 신고 목록 조회 (동적 조건 + 페이징)
//	    HashMap<String, Object> map = new HashMap<>();
//	    map.put("start", 0);
//	    map.put("end", 10);
//	    map.put("status", "PENDING");   // 조건 예시
//	    map.put("targetType", "REVIEW"); // 조건 예시
//	    // map.put("memberId", 5);
//	    // map.put("reasonCode", "SPAM");
//	    // map.put("createdAt", "2026-06-19");
//	    List<ReportsDto> list = mapper.selectAdminReports(map);
//	    System.out.println("selectAdminReports result = " + list);
//	}
//
//	@Ignore //@Test
//	public void testAdminSelectReportsCnt() { // 관리자 신고 목록 카운트
//	    HashMap<String, Object> map = new HashMap<>();
//	    map.put("status", "PENDING");
//	    int cnt = mapper.selectAdminReportsCnt(map);
//	    System.out.println("신고 건수: " + cnt);
//	}
//
//	@Ignore //@Test
//	public void testAdminSelectDetail() { // 단건 조회 (reportId)
//	    HashMap<String, Object> map = new HashMap<>();
//	    map.put("reportId", 6);
//	    List<ReportsDto> detail = mapper.selectAdminReports(map);
//	    System.out.println("selectAdminReports (detail) result = " + detail);
//	}
//
//	
//	// ===== user =====
//	// ===== user =====
//	@Ignore //@Test
//	public void test5() { // �Ű� ���� delete (update delete_yn = 'N')
//		ReportsDto dto = new ReportsDto();
//		dto.setReportId(8);
//		dto.setMemberId(5);
//		mapper.deleteUserReport(dto);
//	}
//	
//	@Ignore //@Test
//	public void test4() { // �Ű� ���� update
//		ReportsDto dto = new ReportsDto();
//		dto.setReportId(8);	// �Ű�no
//		dto.setMemberId(5); // �Ű��� ���
//		dto.setReasonCode("SPAM");
//		dto.setReasonDetail("���� ������ ¯����");
//		mapper.updateUserReport(dto);
//	}
//	
//	@Ignore //@Test
//	public void test3() { // �Ű� �ۼ� insert
//		ReportsDto dto = new ReportsDto();
//		dto.setReportId(6);
//		dto.setTargetType("REVIEW");
//		dto.setTargetId(3);
//		dto.setMemberId(5);
//		dto.setReasonCode("SPAM");
//		dto.setReasonDetail("������ ��");
//		dto.setStatus("PENDING");
//		dto.setCreatedAt("2026-06-18 13:04");
//		mapper.insertUserReport(dto);
//	}
//	
//	@Ignore //@Test
//	public void test2_detail() { // �� �Ű��� �� ��ȸ select detail
//		ReportsDto dto = new ReportsDto();
//		dto.setReportId(4);
//		dto.setMemberId(4);
//		System.out.println( mapper.selectUserReportDetail(dto) );
//	}
//	
//	@Ignore //@Test
//	public void test2() { // �� �Ű��� ��ȸ - ����Ʈ �������� select
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("start", 0);
//		map.put("end"  , 10);
//		map.put("memberId"  , 5);
//		
//		System.out.println(mapper.selectUserReport(map));	//memberid=5 ���� ����Ʈ �˻�
////		System.out.println(mapper.selectUserCnt(4));		//memberid Cnt(*) �˻�
//	}
//
//	@Ignore //@Test
//	public void test1() {
//		System.out.println(".............3. " + sqlSession);
//		System.out.println(".............2. " + ds);
//		System.out.println(".............1. " + context);
//	}
//>>>>>>> bf457620b45c51fa4b68a85344619a851dcfa915
//
//	     dto.setAnswerId(11);
//	     dto.setContent("수정된 답변 내용");
//	     dto.setIsPublic("N");
//
//	     answer.updateAnswer(dto);
//
//	     System.out.println("답변 수정 완료");
//	 }
//	
//	@Ignore @Test // 답변 등록
//	public void insertAnswer() {
//	    AnswerDto dto = new AnswerDto();
//	    
//	    dto.setQuestionId(12);
//	    dto.setMemberId(2);
//	    dto.setContent("JUnit 답변 등록 테스트");
//	    dto.setIsPublic("Y");
//	    
//	    answer.insertAnswer(dto);
//	    
//	    System.out.println("답변 등록 완료");
//	}
//	
//	/////////////////////////////////////////////////////////////////
//	
//	@Ignore @Test  // 오늘 등록 문의 수
//	public void findTodayCnt() {
//	    int count = question.findTodayCnt();
//	    System.out.println( "오늘 등록 문의 수 : " + count );
//	}
//	
//	@Ignore @Test  // 답변 완료 수
//	public void findAnsweredCnt() {
//	    int count = question.findAnsweredCnt();
//	    System.out.println( "답변 완료 수 : " + count );
//	}
//	
//	@Ignore @Test  // 답변 대기 수
//	public void findPendingCnt() {
//	    int count = question.findPendingCnt();
//	    System.out.println( "답변 대기 수 : " + count );
//	}
//	
//	@Ignore @Test  // 전체 문의 수
//	public void findAllCnt() {
//	    int count = question.findAllCnt();
//	    System.out.println( "전체 문의 수 : " + count );
//	}
//	
//	@Ignore @Test // 등록일 검색
//	public void findBySearchDate() {
//	    QuestionDto dto = new QuestionDto();
//	    dto.setCreatedAt( java.sql.Timestamp.valueOf( "2026-06-18 00:00:00") );
//	    System.out.println( question.findBySearch(dto) );
//	}
//	
//	@Ignore @Test // 내용 검색
//	public void findBySearchContent() {
//	    QuestionDto dto = new QuestionDto();
//	    dto.setContent("내용");
//	    System.out.println( question.findBySearch(dto) );
//	}
//	
//	@Ignore @Test // 제목 검색
//	public void findBySearchTitle() {
//	    QuestionDto dto = new QuestionDto();
//	    dto.setTitle("제목");
//	    System.out.println( question.findBySearch(dto) );
//	}
//	
//	@Ignore @Test // 답변 상태 변경
//	public void updateStatusAnswered() {
//	    question.updateStatusAnswered(11);
//	    QuestionDto dto = question.findById(11);
//	    System.out.println("상태 : " + dto.getStatus());
//	}
//	
//	@Ignore @Test // 문의 삭제 (논리 삭제)
//	public void deleteQuestion() {
//	    question.deleteQuestion(12);
//	    System.out.println("삭제 완료");
//	}
//	
//	@Ignore @Test // 문의 수정
//	public void updateQuestion() {
//	    QuestionDto dto = new QuestionDto();
//
//	    dto.setQuestionId(1);
//	    dto.setTitle("수정된 제목");
//        dto.setContent("수정된 내용");
//        dto.setIsPublic("N");
//
//	    question.updateQuestion(dto);
//
//	    System.out.println("수정 완료");
//	}
//	
//	@Ignore @Test // 문의 등록
//	public void insertQuestion() {
//	    QuestionDto dto = new QuestionDto();
//
//	    dto.setParentId(1);
//        dto.setMemberId(1);
//        dto.setCategory("ADMIN");
//        dto.setTitle("JUnit 문의 등록");
//        dto.setContent("문의 등록 테스트입니다.");
//        dto.setIsPublic("Y");
//        dto.setDeleteYn("N");
//
//	    question.insertQuestion(dto);
//
//	    System.out.println("��� �Ϸ�");
//	}
//	
//	@Ignore @Test // 사용자측 문의 목록 페이징
//	public void findMyQuestions() {
//	    Map<String, Object> map = new HashMap<>();
//
//	    map.put("memberId", 1); // 로그인 사용자
//	    map.put("start", 0);    // 시작 위치
//	    map.put("end", 10);     // 페이지당 10개
//
//	    List<QuestionDto> list = question.findMyQuestions(map);
//
//	    System.out.println("===== 내 문의 목록 =====");
//
//	    for (QuestionDto dto : list) {
//	        System.out.println(
//	            dto.getQuestionId() + " / "
//	            + dto.getTitle() + " / "
//	            + dto.getStatus());}
//	}
//	
//	@Test // 관리자측 문의 목록 페이징
//	public void findAll() {
//	    Map<String, Integer> map = new HashMap<>();
//
//	    map.put("start", 0);   // 1페이지
//	    map.put("end", 10);
//
//	    List<QuestionDto> list = question.findAll(map);
//
//	    for(QuestionDto q : list) {
//	        System.out.println(q.getQuestionId()+ " / "+ q.getTitle()+ " / "+ q.getStatus()
//	        );
//	    }
//	}
//	
//	@Ignore @Test // 상세보기
//	public void test1() {
//		System.out.println(question.findById(1));
//	}
//}
