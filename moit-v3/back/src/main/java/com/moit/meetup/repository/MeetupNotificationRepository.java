package com.moit.meetup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.meetup.entity.MeetupNotification;

public interface MeetupNotificationRepository extends JpaRepository<MeetupNotification, Long>{
}
