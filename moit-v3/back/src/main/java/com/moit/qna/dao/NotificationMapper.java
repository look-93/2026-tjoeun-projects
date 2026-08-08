package com.moit.qna.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.moit.qna.dto.NotificationDto;

@Mapper
public interface NotificationMapper {

    int unreadCount(Long memberId);
    void insert(NotificationDto dto);
    void readNotification(Long notificationId);
    List<NotificationDto> selectUnread(Long memberId);
    List<NotificationDto> selectAll(Long memberId);
}
