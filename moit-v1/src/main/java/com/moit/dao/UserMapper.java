package com.moit.dao;

import com.moit.dto.UserDto;
import com.moit.dto.AuthUserDto;
//import org.apache.ibatis.annotations.Mapper;
import java.util.Map;
import java.util.List;

@Mapper
public interface UserMapper {

    // 1. 회원가입 (일반, 제휴업체, 관리자 공통 사용 가능)
    int insert(UserDto dto);

    // 2. 로그인 인증 정보 조회
    AuthUserDto readAuth(Map<String,Object> map);

    // 3. 회원 단건 검색 (아이디 중복검사, 닉네임 중복검사 시 활용)
    UserDto findMember(Map<String, Object> paramMap);

    // 4. 회원 정보 동적 수정 (닉네임/비밀번호/프로필 변경 및 탈퇴 처리, 관리자 승인/권한변경 공통)
    int updateMember(UserDto dto);

    // 5. 회원 리스트 페이징 조회 (조건별 동적 바인딩 가능)
    List<UserDto> select10(Map<String, Object> paramMap);

    // 6. 조건별 전체 회원 수 조회 (페이징 블록 계산용)
    int selectCnt(Map<String, Object> paramMap);

    // 7. 관리자 기능 - 회원 물리 삭제
    int deleteMember(String loginId);
    
    /* security */
    // 로그인
    UserDto findByLoginId(String loginId);
}