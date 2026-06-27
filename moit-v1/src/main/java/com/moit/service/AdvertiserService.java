package com.moit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.dao.AdvertiserMapper;
import com.moit.dto.AdvertiserDto;

@Service
public class AdvertiserService {

    @Autowired
    private AdvertiserMapper advertiserMapper;

    // 광고주 검색
    public List<AdvertiserDto> searchAdvertiser(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        return advertiserMapper.searchAdvertiserByNickname(keyword);
    }
}