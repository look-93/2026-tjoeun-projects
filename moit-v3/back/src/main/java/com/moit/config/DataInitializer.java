package com.moit.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.moit.member.entity.MemberStatus;
import com.moit.member.entity.MemberType;
import com.moit.member.enums.MemberStatusEnum;
import com.moit.member.enums.MemberTypeEnum;
import com.moit.member.repository.MemberStatusRepository;
import com.moit.member.repository.MemberTypeRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
	
	private final MemberTypeRepository memberTypeRepository;
	private final MemberStatusRepository memberStatusRepository;
	
	@Bean
	CommandLineRunner initData() {
		return args -> {
			// 회원유형 초기 데이터
			for(MemberTypeEnum type : MemberTypeEnum.values()) {
				if(!memberTypeRepository.existsById(type.getId())) {
					MemberType memberType = new MemberType();
					
					memberType.setMemberTypeId(type.getId());
					memberType.setTypeName(type.name());
					
					memberTypeRepository.save(memberType);					
				}
			}
			
			// 회원상태 초기 데이터
			for(MemberStatusEnum status : MemberStatusEnum.values()) {
				if(!memberStatusRepository.existsById(status.getId())) {
					MemberStatus memberStatus = new MemberStatus();
					
					memberStatus.setStatusId(status.getId());
					memberStatus.setStatusName(status.name());
					
					memberStatusRepository.save(memberStatus);
				}
			}
		};
	}
	
}
