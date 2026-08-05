package com.moit.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.member.entity.ReportStatus;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long>{

}
