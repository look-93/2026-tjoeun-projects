package com.moit.qna.dao;

import org.apache.ibatis.annotations.Mapper;
import com.moit.qna.dto.QuestionAiAnalysisDto;

@Mapper
public interface QuestionAiAnalysisMapper {

    void insert(QuestionAiAnalysisDto dto);
    void changeToNormal(int questionId);
}