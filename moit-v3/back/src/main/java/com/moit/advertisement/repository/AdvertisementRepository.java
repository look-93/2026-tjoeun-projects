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
import com.moit.advertisement.enums.AdPosition;
import com.moit.advertisement.enums.AdStatus;
import com.moit.advertisement.enums.ApprovalStatus;

public interface AdvertisementRepository
        extends JpaRepository<Advertisement, Long> {
	
	@Query("""
	    select distinct a
	    from Advertisement a
	    join AdvertisementImage ai
	        on ai.advertisement = a
	    where a.deleteYn = 'N'
	      and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED
	      and a.status = com.moit.advertisement.enums.AdStatus.OPEN
	      and a.startDatetime <= CURRENT_TIMESTAMP
	      and a.endDatetime >= CURRENT_TIMESTAMP
	      and ai.imageType = :position
	    order by a.priorityScore desc, a.adId desc
	""")
	List<Advertisement> findAvailableAdvertisements(
	        @Param("position") AdPosition position,
	        Pageable pageable
	);
	
    
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
	
	List<Advertisement> findByDeleteYnAndApprovalStatus(
	        Character deleteYn,
	        ApprovalStatus approvalStatus
	);
	
	List<Advertisement> findByDeleteYn(Character deleteYn);
	
	Optional<Advertisement> findByAdIdAndDeleteYn(
	    Long adId,
	    Character deleteYn
	);
	
	Page<Advertisement> findAll(
	    Specification<Advertisement> spec,
	    Pageable pageable
	);
	
	
	List<Advertisement> findByAdvertiser_IdAndDeleteYn(Long advertiserId, Character deleteYn);

	long countByAdvertiser_IdAndDeleteYn(Long advertiserId, Character deleteYn);

	List<Advertisement> findByAdvertiser_IdAndApprovalStatusAndDeleteYn(
	        Long advertiserId, 
	        ApprovalStatus approvalStatus, 
	        Character deleteYn
	);
	
	long countByApprovalStatus(ApprovalStatus status);
	
	long countByDeleteYn(Character deleteYn);

	long countByDeleteYnAndApprovalStatus(
	        Character deleteYn,
	        ApprovalStatus approvalStatus
	);

	List<Advertisement> findByApprovalStatus(ApprovalStatus approvalStatus);

	long countByStatus(AdStatus status);

	
	long count(Specification<Advertisement> spec);

	
	// =========================================================
    // 관리자 탭별 전용 쿼리 메서드
    // =========================================================

    // 1. 승인 관리 탭: 승인 대기(WAITING)이거나, 승인 완료(APPROVED)인데 결제 대기(WAITING)인 경우
    @Query("""
        select a from Advertisement a 
        where a.deleteYn = 'N' 
          and (a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.WAITING 
               or (a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED 
                   and a.paymentStatus = 'WAITING'))
        order by a.createdAt desc
    """)
    Page<Advertisement> findApprovalTabList(Pageable pageable);

    @Query("""
        select count(a) from Advertisement a 
        where a.deleteYn = 'N' 
          and (a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.WAITING 
               or (a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED 
                   and a.paymentStatus = 'WAITING'))
    """)
    long countApprovalTabList();


    // 3. 운영 관리 탭: 승인 완료(APPROVED)되고 결제 완료(PAID)된 정상 운영 대상
    @Query("""
        select a from Advertisement a 
        where a.deleteYn = 'N' 
          and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED 
          and a.paymentStatus = 'PAID'
        order by a.createdAt desc
    """)
    Page<Advertisement> findStatusTabList(Pageable pageable);

    @Query("""
        select count(a) from Advertisement a 
        where a.deleteYn = 'N' 
          and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED 
          and a.paymentStatus = 'PAID'
    """)
    long countStatusTabList();
}