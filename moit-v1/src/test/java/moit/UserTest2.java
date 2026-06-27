package moit;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.moit.dao.ReportsMapper;
import com.moit.dto.ReportsDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:config/root-context.xml"   , 
		"classpath:config/security-context.xml" 
})
public class UserTest2 {
	@Autowired   ApplicationContext context;
	@Autowired   DataSource         ds;
	@Autowired   SqlSession         sqlSession;
	
	@Autowired ReportsMapper	mapper;
	
	// ===== admin =====
	// ===== admin =====
	@Ignore //@Test
	public void testAdminUpdateStatus() { // 신고 상태 변경
	    ReportsDto dto = new ReportsDto();
	    dto.setReportId(8);       // 테스트할 reportId
	    dto.setStatus("APPROVED"); // 바꿀 상태
	    mapper.updateAdmin(dto);
	}

	@Ignore //@Test
	public void testAdminDelete() { // 신고 강제 삭제
	    mapper.deleteAdmin(8); // reportId = 8 삭제
	}

	@Ignore //@Test
	public void testAdminSelectReports() { // 관리자 신고 목록 조회 (동적 조건 + 페이징)
	    HashMap<String, Object> map = new HashMap<>();
	    map.put("start", 0);
	    map.put("end", 10);
	    // 조건 예시
	    map.put("status", "PENDING");
	    map.put("targetType", "REVIEW");
	    // map.put("memberId", 5);
	    // map.put("reasonCode", "SPAM");
	    // map.put("createdAt", "2026-06-19");
	    List<ReportsDto> list = mapper.selectAdminReports(map);
	    System.out.println(list);
	}

	@Ignore //@Test
	public void testAdminSelectReportsCnt() { // 관리자 신고 목록 카운트
	    HashMap<String, Object> map = new HashMap<>();
	    map.put("status", "PENDING");
	    int cnt = mapper.selectAdminReportsCnt(map);
	    System.out.println("신고 건수: " + cnt);
	}

	@Ignore //@Test
	public void testAdminSelectDetail() { // 단건 조회 (reportId)
	    HashMap<String, Object> map = new HashMap<>();
	    map.put("reportId", 8);
	    List<ReportsDto> detail = mapper.selectAdminReports(map);
	    System.out.println(detail);
	}

	// ===== user =====
	// ===== user =====
	@Ignore //@Test
	public void test5() { // �Ű� ���� delete (update delete_yn = 'N')
		ReportsDto dto = new ReportsDto();
		dto.setReportId(8);
		dto.setMemberId(5);
		mapper.deleteUserReport(dto);
	}
	
	@Ignore //@Test
	public void test4() { // �Ű� ���� update
		ReportsDto dto = new ReportsDto();
		dto.setReportId(8);	// �Ű�no
		dto.setMemberId(5); // �Ű��� ���
		dto.setReasonCode("SPAM");
		dto.setReasonDetail("���� ������ ¯����");
		mapper.updateUserReport(dto);
	}
	
	@Ignore //@Test
	public void test3() { // �Ű� �ۼ� insert
		ReportsDto dto = new ReportsDto();
		dto.setReportId(6);
		dto.setTargetType("REVIEW");
		dto.setTargetId(3);
		dto.setMemberId(5);
		dto.setReasonCode("SPAM");
		dto.setReasonDetail("������ ��");
		dto.setStatus("PENDING");
		dto.setCreatedAt("2026-06-18 13:04");
		mapper.insertUserReport(dto);
	}
	
	@Ignore //@Test
	public void test2_detail() { // �� �Ű��� �� ��ȸ select detail
		ReportsDto dto = new ReportsDto();
		dto.setReportId(4);
		dto.setMemberId(4);
		System.out.println( mapper.selectUserReportDetail(dto) );
	}
	
	@Ignore //@Test
	public void test2() { // �� �Ű��� ��ȸ - ����Ʈ �������� select
		HashMap<String, Object> map = new HashMap<>();
		map.put("start", 0);
		map.put("end"  , 10);
		map.put("memberId"  , 5);
		
		System.out.println(mapper.selectUserReport(map));	//memberid=5 ���� ����Ʈ �˻�
//		System.out.println(mapper.selectUserCnt(4));		//memberid Cnt(*) �˻�
	}

	@Ignore //@Test
	public void test1() {
		System.out.println(".............3. " + sqlSession);
		System.out.println(".............2. " + ds);
		System.out.println(".............1. " + context);
	}
}
