package com.moit.advertisement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moit.advertisement.entity.Advertisement;
import com.moit.advertisement.enums.AdStatus;
import com.moit.advertisement.enums.ApprovalStatus;

public interface AdvertisementRepository
        extends JpaRepository<Advertisement, Long> {
	
    
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
	    update Advertisement a
	       set a.impressions = a.impressions + 1
	     where a.adId = :adId
	""")
	int increaseImpressions(@Param("adId") Long adId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
	    update Advertisement a
	       set a.clicks = a.clicks + 1
	     where a.adId = :adId
	""")
	int increaseClicks(@Param("adId") Long adId);
	
	Page<Advertisement> findByDeleteYnAndApprovalStatus(
	    String deleteYn,
	    ApprovalStatus approvalStatus,
	    Pageable pageable
	);
	
	Optional<Advertisement> findByAdIdAndDeleteYn(
	    Long adId,
	    String deleteYn
	);
	
	Page<Advertisement> findAll(
	    Specification<Advertisement> spec,
	    Pageable pageable
	);
	
	List<Advertisement> findByAdvertiser_Id(Long id);

	List<Advertisement> findByApprovalStatus(ApprovalStatus approvalStatus);

	List<Advertisement> findByStatus(AdStatus status);

	List<Advertisement> findByAdvertiser_IdAndApprovalStatus(
	        Long id,
	        ApprovalStatus approvalStatus
	);
	
	long count(Specification<Advertisement> spec);

}