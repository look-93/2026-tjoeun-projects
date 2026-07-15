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

    public List<NotificationDto> selectUnread(int memberId) {
        return notificationMapper.selectUnread(memberId);
    }

    public List<NotificationDto> selectAll(int memberId){
        return notificationMapper.selectAll(memberId);
    }
    
    public void readNotification(int notificationId) {
        notificationMapper.readNotification(notificationId);
    }
    
    public int unreadCount(int memberId){
        return notificationMapper.unreadCount(memberId);
    }

}