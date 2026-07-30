package com.moit.qna.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.moit.qna.dto.NotificationDto;

@Mapper
public interface NotificationMapper {

    int unreadCount(int memberId);
    void insert(NotificationDto dto);
    void readNotification(int notificationId);
    List<NotificationDto> selectUnread(int memberId);
    List<NotificationDto> selectAll(int memberId);
}
