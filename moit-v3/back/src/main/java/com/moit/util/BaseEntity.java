package com.moit.util;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;

public abstract class BaseEntity {
	@Column
	private Boolean deleteYn;
	
	@CreationTimestamp
	@Column
	private LocalDateTime createdAt;
	
	@CreationTimestamp //처음에 생셩되는 날짜
	@LastModifiedDate  //마지막 변경된 날짜
	@Column
	private LocalDateTime updatedAt;
}
