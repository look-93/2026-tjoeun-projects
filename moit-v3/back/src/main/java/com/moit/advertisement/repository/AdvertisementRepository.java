package com.moit.advertisement.repository;

import java.time.LocalDateTime;
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
import com.moit.advertisement.enums.PaymentStatus;

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
	
	long countByDeleteYnAndStatus(Character deleteYn, AdStatus status);
	
	// 삭제 안됨 + 결제 상태 + 운영 상태
	long countByDeleteYnAndPaymentStatusAndStatus(Character deleteYn, PaymentStatus paymentStatus, AdStatus status);

	
	// =========================================================
    // 관리자 탭별 전용 쿼리 메서드
    // =========================================================

	// 1. 승인 관리 탭 
    @Query("""
        select a from Advertisement a 
        where a.deleteYn = 'N' 
          and (
              (:status is null or :status = 'all' or :status = '') and (
                  a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.WAITING 
                  or (a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED and a.paymentStatus = 'WAITING')
              )
              or (:status = 'WAITING' and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.WAITING)
              or (:status = 'PAYMENT_WAITING' and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED and a.paymentStatus = 'WAITING')
              or (:status = 'REJECTED' and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.REJECTED)
          )
          and (:searchText is null or :searchText = '' or a.title like %:searchText%)
    """)
    Page<Advertisement> findApprovalTabList(
        @Param("searchText") String searchText,
        @Param("status") String status, 
        Pageable pageable
    );

    @Query("""
        select count(a) from Advertisement a 
        where a.deleteYn = 'N' 
          and (
              (:status is null or :status = 'all' or :status = '') and (
                  a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.WAITING 
                  or (a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED and a.paymentStatus = 'WAITING')
              )
              or (:status = 'WAITING' and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.WAITING)
              or (:status = 'PAYMENT_WAITING' and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED and a.paymentStatus = 'WAITING')
              or (:status = 'REJECTED' and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.REJECTED)
          )
          and (:searchText is null or :searchText = '' or a.title like %:searchText%)
    """)
    long countApprovalTabList(
        @Param("searchText") String searchText,
        @Param("status") String status
    );

    // 3. 운영 관리 탭
    @Query("""
        select a from Advertisement a 
        where a.deleteYn = 'N' 
          and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED 
          and a.paymentStatus = 'PAID'
          and (:searchText is null or :searchText = '' or a.title like %:searchText%)
          and (:status is null or :status = '' or 
               (:status = 'BEFORE_OPEN' and a.status = com.moit.advertisement.enums.AdStatus.PENDING) or
               (:status = 'OPEN' and a.status = com.moit.advertisement.enums.AdStatus.OPEN) or
               (:status = 'CLOSED' and a.status = com.moit.advertisement.enums.AdStatus.CLOSED))
    """)
    Page<Advertisement> findStatusTabList(
        @Param("searchText") String searchText,
        @Param("status") String status,
        Pageable pageable
    );

    @Query("""
        select count(a) from Advertisement a 
        where a.deleteYn = 'N' 
          and a.approvalStatus = com.moit.advertisement.enums.ApprovalStatus.APPROVED 
          and a.paymentStatus = 'PAID'
          and (:searchText is null or :searchText = '' or a.title like %:searchText%)
          and (:status is null or :status = '' or 
               (:status = 'BEFORE_OPEN' and a.status = com.moit.advertisement.enums.AdStatus.PENDING) or
               (:status = 'OPEN' and a.status = com.moit.advertisement.enums.AdStatus.OPEN) or
               (:status = 'CLOSED' and a.status = com.moit.advertisement.enums.AdStatus.CLOSED))
    """)
    long countStatusTabList(
        @Param("searchText") String searchText,
        @Param("status") String status
    );
    
    // 스케줄러용: 특정 상태이면서 시작시간이 특정 시간(현재) 이전인 광고 조회
    List<Advertisement> findByStatusAndStartDatetimeLessThanEqual(AdStatus status, LocalDateTime now);

    // 스케줄러용: 특정 상태이면서 종료시간이 특정 시간(현재) 이전인 광고 조회
    List<Advertisement> findByStatusAndEndDatetimeLessThanEqual(AdStatus status, LocalDateTime now);
    
    // 스케줄러용: 종료시간이 30일, 14일 남은 광고 조회
    List<Advertisement> findByDeleteYnAndPaymentStatusAndStatusAndEndDatetimeBetween(
            Character deleteYn,
            PaymentStatus paymentStatus,
            AdStatus status,
            LocalDateTime start,
            LocalDateTime end
    );
    
    @Query("""
	    select a
	    from Advertisement a
	    where a.deleteYn = 'N'
	      and a.paymentStatus = com.moit.advertisement.enums.PaymentStatus.PAID
	      and a.status = com.moit.advertisement.enums.AdStatus.OPEN
	      and a.endDatetime between :start and :end
	      and a.reminder30dSent = 'N'
	""")
	List<Advertisement> findReminder30Advertisements(
	        @Param("start") LocalDateTime start,
	        @Param("end") LocalDateTime end
	);

	@Query("""
	    select a
	    from Advertisement a
	    where a.deleteYn = 'N'
	      and a.paymentStatus = com.moit.advertisement.enums.PaymentStatus.PAID
	      and a.status = com.moit.advertisement.enums.AdStatus.OPEN
	      and a.endDatetime between :start and :end
	      and a.reminder14dSent = 'N'
	""")
	List<Advertisement> findReminder14Advertisements(
	        @Param("start") LocalDateTime start,
	        @Param("end") LocalDateTime end
	);
}