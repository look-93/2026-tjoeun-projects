package com.moit.util;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
	
	@Builder.Default
	@Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
	private Character deleteYn = 'N';
	
	@CreationTimestamp
	@Column
	private LocalDateTime createdAt;
	
	@UpdateTimestamp  
	@Column
	private LocalDateTime updatedAt;
}
