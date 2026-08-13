package com.moit.qna.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.moit.qna.dto.QuestionDto.QuestionRequestDto;
import com.moit.qna.dto.QuestionDto.QuestionResponseDto;
import com.moit.qna.dto.NotificationDto;
import com.moit.qna.dto.QuestionAiAnalysisDto;
import com.moit.qna.dto.QuestionSearchDto;
import com.moit.qna.dto.AnswerDto.AnswerRequestDto;
import com.moit.qna.dto.AnswerDto.AnswerResponseDto;

@Mapper
public interface QuestionMapper {

    // 전체 문의 목록 조회
    List<QuestionResponseDto> findAll(Map<String, Object> map);

    // 문의 상세 조회
    QuestionResponseDto findById(Long questionId);

    // 문의 등록
    void insertQuestion(QuestionRequestDto dto);

    // 답변 등록 시 문의 상태 변경
    void updateStatusAnswered(Long questionId);

    // 문의 수정
    void updateQuestion(QuestionRequestDto dto);

    // 문의 삭제
    void deleteQuestion(Long questionId);

    // 검색
    List<QuestionResponseDto> findBySearch(QuestionSearchDto dto);

    // 전체 문의 수
    int findAllCnt();

    // 검색 결과 수
    int findSearchCnt(Map<String, Object> map);

    // 답변 대기 수
    int findPendingCnt();

    // 답변 완료 수
    int findAnsweredCnt();

    // 오늘 등록 문의 수
    int findTodayCnt();

    // 사용자 문의 목록 조회
    List<QuestionResponseDto> findMyQuestions(Map<String, Object> map);

    // 내 문의 총 개수 조회
    int findMyQuestionCnt(Map<String, Object> map);

    // 답변 삭제 시 문의 상태 변경
    void updateStatusPending(Long questionId);

    // 관리자용 선택 삭제
    void deleteSelected(List<Long> ids);

    // 특정 모임 문의 조회
    List<QuestionResponseDto> selectByParentId(Long parentId);
    
    
    // =====  답변  =====
    // 질문에 대한 답변 조회
    AnswerResponseDto findByQuestionId(Long questionId);

    // 답변 등록
    void insertAnswer(AnswerRequestDto dto);

    // 답변 수정
    void updateAnswer(AnswerRequestDto dto);

    // 답변 삭제
    void deleteAnswer(Long answerId);

    // 삭제된 답변 조회
    AnswerResponseDto findByQuestionIdAll(Long questionId);

    // 삭제된 답변 복구용
    void restoreAnswer(AnswerRequestDto dto);
    
    
    // =====  Ai 공격성  =====
    void insertAiAnalysis(QuestionAiAnalysisDto dto);
    void changeToNormal(Long questionId);

    
    // =====  답변 알림(비동기)  =====
    int unreadCount(Long memberId);
    void insertNotification(NotificationDto dto);
    void readNotification(Long notificationId, Long memberId);
    void deleteOldestNotification(Long memberId);
    int countNotifications(Long memberId);
    List<NotificationDto> selectUnread(Long memberId);
    List<NotificationDto> selectAll(Long memberId);
}