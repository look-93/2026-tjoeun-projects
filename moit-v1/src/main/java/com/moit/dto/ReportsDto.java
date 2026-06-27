package com.moit.dto;

import lombok.Data;

@Data
public class ReportsDto {
	private int reportId;		//신고 고유 ID
	private String targetType;	//�Ű������� (����, �ı�) 'MEETUP', 'REVIEW'
	private int targetId;		//�Ű���id (����id or �ı�id)
	private int memberId;		//�Ű��� ȸ��id
	private String reasonCode;	//�Ű� ���� �ڵ� ('ABUSE', 'SPAM', 'FAKE_INFO', 'AD', 'ETC')
	private String reasonDetail;//�� ���� (etc = ���������� ���)
	private String status;		//ó������ ('PENDING', 'APPROVED')
	private String deleteYn;	//��������
	private String createdAt;	//�����Ͻ�
	private String updatedAt;	//�����Ͻ�
	
//	private int reportCount;	// �Ű� �Ǽ�
}
