package com.moit.qna.dto;
 
import lombok.Data;

@Data
public class NotificationDto {

    private int notificationId;
    private int questionId;
    private int memberId;
    private String type;
    private String message;
    private String isRead;
    private java.sql.Timestamp createdAt;
}
