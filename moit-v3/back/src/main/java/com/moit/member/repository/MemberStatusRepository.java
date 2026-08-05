package com.moit.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.member.entity.MemberStatus;

@Repository
public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long>{

}
