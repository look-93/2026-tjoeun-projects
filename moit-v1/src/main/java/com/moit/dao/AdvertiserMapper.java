package com.moit.dao;

import java.util.List;
import com.moit.dto.AdvertiserDto;

@Mapper
public interface AdvertiserMapper {

    // 광고주 자동완성 (PARTNER만)
	public List<AdvertiserDto> searchAdvertiserByNickname(String keyword);
}