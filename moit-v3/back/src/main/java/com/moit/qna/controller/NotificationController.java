package com.moit.qna.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moit.qna.dto.NotificationDto;
import com.moit.qna.service.NotificationService;
import com.moit.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public List<NotificationDto> list(
            @AuthenticationPrincipal CustomUserDetails loginUser){
    	return notificationService.selectAll(loginUser.getAppUserId());
    }

    @GetMapping("/notifications/count")
    public int count(
            @AuthenticationPrincipal CustomUserDetails loginUser){
        return notificationService.unreadCount(loginUser.getAppUserId());
    }
    
    @PostMapping("/notifications/read")
    public void read(@RequestParam int notificationId){
        notificationService.readNotification(notificationId);
    }
    
    
}