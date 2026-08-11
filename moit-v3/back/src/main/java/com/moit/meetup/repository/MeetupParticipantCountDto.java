package com.moit.meetup.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetupParticipantCountDto {

    private Long meetupId;
    private Long totalParticipants;
}
