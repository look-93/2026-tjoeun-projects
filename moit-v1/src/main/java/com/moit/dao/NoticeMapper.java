package com.moit.dao;

import java.util.List;
import com.moit.dto.NoticeDto;

@Mapper
public interface NoticeMapper {
	
	List<NoticeDto> selectnoticelist();
}
