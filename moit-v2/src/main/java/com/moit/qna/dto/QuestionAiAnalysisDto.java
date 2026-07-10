package com.moit.qna.dto; 

import lombok.Data; 

@Data public class QuestionAiAnalysisDto { 
	private int questionId; 
	private String analysisStatus; 
	private int aggressionScore; 
	
}