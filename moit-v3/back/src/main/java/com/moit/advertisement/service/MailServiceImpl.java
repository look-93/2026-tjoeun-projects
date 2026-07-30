package com.moit.advertisement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.moit.advertisement.dto.AdvertisementDto;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailSenderEmail;

    @Override
    public void sendAdvertisementReminderMail(AdvertisementDto ad, int remainDay) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailSenderEmail);
            helper.setTo(ad.getEmail());
            helper.setSubject("[MOIT] 광고 종료 예정 안내");

            String html = """
                    <html>
                    <body style="font-family:Arial,sans-serif;background:#f5f5f5;padding:30px;">

                    <div style="
                        max-width:600px;
                        margin:auto;
                        background:white;
                        border-radius:10px;
                        padding:40px;
                        border:1px solid #e5e5e5;">

                        <h2 style="color:#0d6efd;margin-top:0;">
                            광고 종료 예정 안내
                        </h2>

                        <p>
                            안녕하세요.
                            <b>%s</b>님.
                        </p>

                        <p>
                            등록하신 광고
                            <b>『%s』</b>의 종료일까지
                            <span style="color:red;font-weight:bold;">%d일</span>
                            남았습니다.
                        </p>

                        <p>
                            광고를 계속 게시하시려면 아래 버튼을 눌러
                            광고 연장을 신청해주세요.
                        </p>

                        <div style="margin:35px 0;text-align:center;">

                            <a href="http://localhost:8080/user/advertisement/list"
                               style="
                                    display:inline-block;
                                    padding:14px 28px;
                                    background:#0d6efd;
                                    color:#ffffff;
                                    text-decoration:none;
                                    border-radius:6px;
                                    font-weight:bold;">
                                광고 연장 신청하기
                            </a>

                        </div>

                        <hr>

                        <p style="font-size:13px;color:#888;">
                            본 메일은 MOIT 시스템에서 자동 발송되었습니다.
                        </p>

                    </div>

                    </body>
                    </html>
                    """
                    .formatted(
                            ad.getMemberName(),
                            ad.getTitle(),
                            remainDay
                    );

            helper.setText(html, true);

            mailSender.send(message);

            System.out.println("메일 발송 성공");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}