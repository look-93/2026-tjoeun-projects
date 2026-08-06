package com.moit.qna.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moit.qna.dao.NotificationMapper;
import com.moit.qna.dto.NotificationDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationMapper notificationMapper;

    public List<NotificationDto> selectUnread(Long memberId) {
        return notificationMapper.selectUnread(memberId);
    }

    public List<NotificationDto> selectAll(Long memberId){
        return notificationMapper.selectAll(memberId);
    }
    
    public void readNotification(Long notificationId) {
        notificationMapper.readNotification(notificationId);
    }
    
    public int unreadCount(Long memberId){
        return notificationMapper.unreadCount(memberId);
    }

}