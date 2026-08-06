package com.moit.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.moit.reports.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

	
}

/*
save()
findById()
findAll()
deleteById()
existsById()
count()
*/