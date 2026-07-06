package com.moit.meetup.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.moit.meetup.dto.QuestionDto;

@Mapper
public interface QuestionMapper {

    // 전체 문의 목록 조회
    List<QuestionDto> findAll(Map<String, Integer> map);

    // 문의 상세 조회
    QuestionDto findById(int questionId);

    // 문의 등록
    void insertQuestion(QuestionDto dto);

    // 답변 등록 시 문의 상태 변경
    void updateStatusAnswered(int questionId);

    // 문의 수정
    void updateQuestion(QuestionDto dto);

    // 문의 삭제
    void deleteQuestion(int questionId);

    // 제목, 내용, 답변여부 검색
    List<QuestionDto> findBySearch(QuestionDto dto);

    // 전체 문의 수
    int findAllCnt();

    // 답변 대기 수
    int findPendingCnt();

    // 답변 완료 수
    int findAnsweredCnt();

    // 오늘 등록 문의 수
    int findTodayCnt();
    
    // 사용자 문의 목록 조회
    List<QuestionDto> findMyQuestions(Map<String, Object> map);
    
    // 내 문의 총 개수 조회
    int findMyQuestionCnt(int memberId);
    
    // 답변 삭제 시 문의 상태 변경
    void updateStatusPending(int questionId);
    
    //
    List<QuestionDto> selectByParentId(int parentId);
}