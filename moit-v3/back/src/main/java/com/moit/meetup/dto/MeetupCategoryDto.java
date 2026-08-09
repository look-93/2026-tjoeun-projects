package com.moit.meetup.dto;


import com.moit.meetup.entity.MeetupCategory;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetupCategoryDto {
	private Long categoryId;
	private Long parentId;
	private String categoryName;
	
	public static MeetupCategoryDto from(MeetupCategory meetupCategory) {
		
		Long parentId = meetupCategory.getParent() != null ? meetupCategory.getParent().getId() : null; 
		
		return MeetupCategoryDto.builder()
						  .categoryId(meetupCategory.getId())
						  .categoryName(meetupCategory.getCategoryName())
						  .parentId(parentId)
						  .build();
	}
	
}
