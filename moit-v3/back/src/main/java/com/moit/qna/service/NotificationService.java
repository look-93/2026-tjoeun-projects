package com.moit.qna.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moit.qna.dao.QuestionMapper;
import com.moit.qna.dto.NotificationDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final QuestionMapper questionMapper;

    public List<NotificationDto> selectUnread(Long memberId) {
        return questionMapper.selectUnread(memberId);
    }

    public List<NotificationDto> selectAll(Long memberId) {
        return questionMapper.selectAll(memberId);
    }

    public void readNotification(Long notificationId, Long memberId) {
        questionMapper.readNotification(notificationId, memberId);
    }

    public int unreadCount(Long memberId) {
        return questionMapper.unreadCount(memberId);
    }
}