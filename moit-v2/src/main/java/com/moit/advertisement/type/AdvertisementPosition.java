package com.moit.advertisement.type;

import lombok.Getter;

@Getter
public enum AdvertisementPosition {

    MAIN("메인"),
    MEETUP_LIST_BANNER("모임 목록 배너"),
    MEETUP_LIST_SIDEBAR("모임 목록 사이드바"),
    MEETUP_DETAIL_SIDEBAR("모임 상세 사이드바");

    private final String description;

    AdvertisementPosition(String description) {
        this.description = description;
    }
}