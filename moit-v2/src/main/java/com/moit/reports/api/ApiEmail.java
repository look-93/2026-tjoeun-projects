package com.moit.reports.api;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiEmail {
	
	@Value("${naver.host}")
	public String host;
	@Value("${naver.user}")
	public String user;
	@Value("${naver.password}")
	public String password;
	
	
	// 2. 이메일 보내기				제목				본문내용		받는사람
	public void sendMail(String subject, String content, String to) {
		Properties props = new Properties();
		props.put("mail.smtp.host", host); // 어떤 메일 서버 쓸지.
		props.put("mail.smtp.auth", "true"); // 아이디/비밀번호 인증 사용 여부
		props.put("mail.smtp.port", "587"); // 포트
//		props.put("mail.debug", "전송완료"); // 전송 로그 출력

		props.put("mail.smtp.starttls.enable", "true"); // 이메일 전송 시 보안연결 TLS 사용
		props.put("mail.smtp.ssl.trust", "smtp.naver.com"); // ssl 인증서 연결
		props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // TLS 버전 지정
	
		// 3.
		Session session = Session.getInstance(props, new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
			
		// 4. 메일보내기 (Mime 텍스트 text/plain , html text/html , 이미지 image/png) 멀티미디어메시지
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(user)); // 보내는 사람
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 받는 사람
			message.setSubject( subject ); // 제목
			message.setContent( content
								+ "<br>(이 메일은 자동 발송된 안내 메일입니다.)"
								, "text/html; charset=UTF-8");
			
			Transport.send(message);
			System.out.println("....... sendEmail successfully .......");
	
		} catch (Exception e) { e.printStackTrace(); }

	}
}