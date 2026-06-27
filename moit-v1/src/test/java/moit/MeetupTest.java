package moit;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.moit.dao.MeetupMapper;
import com.moit.service.AdminMeetupService;
import com.moit.service.UserMeetupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:config/root-context.xml"   , 
		"classpath:config/security-context.xml" 
})
public class MeetupTest {
	@Autowired ApplicationContext context;
	@Autowired DataSource         ds;
	@Autowired SqlSession         sqlSession;
	@Autowired AdminMeetupService adminMeetupService;
	@Autowired UserMeetupService userMeetupService;
	
	
	@Test
	public void test1() {
		System.out.println(userMeetupService.findAllCategory());
	}
	
}
