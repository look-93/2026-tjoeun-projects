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
	
}
