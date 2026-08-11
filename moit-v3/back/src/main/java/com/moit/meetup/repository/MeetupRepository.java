package com.moit.meetup.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.moit.meetup.dto.MeetupCountResponseDto;
import com.moit.meetup.entity.Meetup;

@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Long>{
	Page<Meetup> findAll(Pageable pageable);
	
	
	
	Page<Meetup> findByMember_Id(Long memberId, Pageable  pageable);
	
	//관리자 통계
	@Query("""
			SELECT new com.moit.meetup.dto.MeetupCountResponseDto(
			    COUNT(CASE WHEN m.meetupStatus  = com.moit.meetup.enums.MeetupStatus.RECRUITING THEN 1 END),
			    COUNT(CASE WHEN m.meetupStatus  = com.moit.meetup.enums.MeetupStatus.COMPLETED THEN 1 END),
			    COUNT(CASE WHEN m.meetupStatus  = com.moit.meetup.enums.MeetupStatus.CANCELED THEN 1 END),
			    COUNT(CASE WHEN m.meetupStatus  = com.moit.meetup.enums.MeetupStatus.WEATHER_CANCELED THEN 1 END)
			)
			FROM Meetup m
			WHERE m.deleteYn = 'N'
			""")
	MeetupCountResponseDto getMeetupCount();
}


//<select id="findAllMeetupBy" parameterType="MeetupSearchDto" resultType="MeetupDto">
//SELECT A.*, C.total_participants,
//<!--  총 참가자 수  -->
//B.nickname, CASE WHEN A.status = 'RECRUITING' THEN '모집중' WHEN A.status = 'CLOSED' THEN '모집마감' END AS status_name, D.name AS sigungu_name, E.name AS sido_name, TO_CHAR(A.meetup_at, 'YYYY.MM.DD') AS formatted_meetup_at, F.category_name,
//<!--  좋아요 수 추가 -->
//CASE WHEN H.member_id IS NULL THEN 0 ELSE 1 END AS has_like, I.like_cnt, j.image_path FROM meetups A LEFT JOIN members B ON B.member_id = A.member_id LEFT JOIN (SELECT meetup_id, COUNT(*) AS total_participants FROM meetup_applications WHERE 1=1 AND status ='APPROVED'
//<!--  승인된 참가자만 카운트  -->
//AND delete_yn = 'N' GROUP BY meetup_id ) C ON C.meetup_id = A.meetup_id LEFT JOIN sigungus D ON D.sigungu_id = A.sigungu_id LEFT JOIN sidos E ON E.sido_id = D.sido_id LEFT JOIN categories F ON F.category_id = A.category_id LEFT JOIN categories G ON G.category_id = F.parent_id LEFT JOIN meetup_likes H ON H.meetup_id = A.meetup_id AND H.member_id = #{memberId, jdbcType=INTEGER} LEFT JOIN (SELECT COUNT(meetup_id) as like_cnt, meetup_id FROM meetup_likes GROUP BY meetup_id ) I ON I.meetup_id = A.meetup_id
//<!--  이미지 1장만 추출하도록 서브쿼리  -->
//LEFT JOIN ( SELECT meetup_id, image_path FROM ( SELECT mi.meetup_id, i.image_path, ROW_NUMBER() OVER (PARTITION BY mi.meetup_id ORDER BY mi.image_id ASC) as rn FROM MEETUP_IMAGES mi JOIN IMAGES i ON mi.image_id = i.image_id ) WHERE rn = 1 ) J ON J.meetup_id = A.meetup_id WHERE A.delete_yn = 'N' AND A.status IN ( 'RECRUITING', 'CLOSED')
//<if test="status != null and status != ''"> AND A.status = #{status} </if>
//<if test="searchType == 'title' and adminSearchText != null and adminSearchText != ''"> AND A.title LIKE '%' || #{adminSearchText} || '%' </if>
//<if test="searchType == 'name' and adminSearchText != null and adminSearchText != ''"> AND B.nickname LIKE'%' || #{adminSearchText} || '%' </if>
//<if test="searchText != null and searchText != ''"> AND A.title LIKE '%' || #{searchText} || '%' </if>
//<if test="sidoId != null and sidoId != 0"> AND E.sido_id = #{sidoId} </if>
//<if test="categoryId != null and categoryId != 0"> AND G.category_id = #{categoryId} </if>
//ORDER BY
//<choose>
//<when test="orderType == 'createAt' and orderType != ''"> A.created_at DESC </when>
//<when test="orderType == 'like' and orderType != ''"> NVL(I.like_cnt,0) DESC </when>
//<when test="orderType == 'meetupAt' and orderType != ''"> CASE WHEN A.status = 'CLOSED' THEN 1 ELSE 0 END ASC, A.meetup_at ASC </when>
//<otherwise> A.created_at DESC </otherwise>
//</choose>
//OFFSET #{start} ROWS FETCH NEXT #{end} ROWS ONLY
//</select>