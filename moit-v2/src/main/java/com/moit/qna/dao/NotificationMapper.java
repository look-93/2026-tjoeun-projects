package com.moit.qna.dao;

import org.apache.ibatis.annotations.Mapper;
import com.moit.qna.dto.NotificationDto;

@Mapper
public interface NotificationMapper {

    void insert(NotificationDto dto);

}
