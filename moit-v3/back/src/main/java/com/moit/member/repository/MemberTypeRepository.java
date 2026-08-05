package com.moit.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.member.entity.MemberType;

@Repository
public interface MemberTypeRepository extends JpaRepository<MemberType, Long>{

}
