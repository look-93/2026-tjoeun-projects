package com.moit.qna.dto;

import lombok.Data;

@Data
public class AnswerDto {

	private int answerId;
	private int questionId;
	private int memberId;

	private String content;
	private String isPublic;
	private String deleteYn;

	private java.sql.Timestamp createdAt;
	private java.sql.Timestamp updatedAt;

	//
	private String memberName;
}