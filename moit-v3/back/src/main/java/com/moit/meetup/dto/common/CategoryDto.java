package com.moit.meetup.dto.common;

import lombok.Data;

@Data
public class CategoryDto {
	private int categoryId;
	private int parentId;
	private String categoryName;
}
