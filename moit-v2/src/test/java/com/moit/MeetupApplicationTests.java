package com.moit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.meetup.dao.TestDao;

@SpringBootTest
public class MeetupApplicationTests {
	@Autowired TestDao dao;
	
	@Test
	public void test1(){
		System.out.println(dao.readTime() + "ddd");
	}
}
