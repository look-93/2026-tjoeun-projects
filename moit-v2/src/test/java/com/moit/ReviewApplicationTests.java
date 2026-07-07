package com.moit;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewApplicationTests {
	@Autowired SqlSession sqlsession;
	
	@Disabled @Test
	public void db_test() {
		// sqlsession.getConfiguration()을 통해 커넥션 정보를 가져옵니다.
        String url = sqlsession.getConfiguration().getEnvironment().getDataSource().toString();
        
        System.out.println("연결된 DB 정보: " + url);
        System.out.println("👍 moit 계정으로 연결 성공!");
	}
	
	
}
