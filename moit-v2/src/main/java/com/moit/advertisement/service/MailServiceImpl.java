package com.moit.advertisement.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.moit.advertisement.dto.AdvertisementDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
	
	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String mailSenderEmail;
	
	@Override
	public void sendAdvertisementReminderMail(AdvertisementDto ad, int remainDay) {
		
	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setFrom(mailSenderEmail);
	    
	    message.setTo(ad.getEmail());

	    message.setSubject("[MOIT] 광고 종료 예정 안내");

	    StringBuilder sb = new StringBuilder();

	    sb.append("안녕하세요, ");
	    sb.append(ad.getMemberName());
	    sb.append(" 사용자님.\n\n");

	    sb.append("등록하신 광고\n\n");

	    sb.append("『");
	    sb.append(ad.getTitle());
	    sb.append("』\n\n");

	    sb.append("의 게시 종료일까지 ");

	    sb.append(remainDay);

	    sb.append("일 남았습니다.\n\n");

	    sb.append("광고를 계속 게시하시려면\n");
	    sb.append("광고 관리 페이지에서 기간을 수정해 연장 신청을 진행해주세요.\n\n");

	    sb.append("감사합니다.\n");

	    message.setText(sb.toString());

	    try {
	        mailSender.send(message);
	        System.out.println("메일 발송 성공");
	    } catch (Exception e) {
	    	 e.printStackTrace();
	    }
	}

}
