package com.kcc.trioffice.domain.common.service;

import com.kcc.trioffice.domain.schedule.dto.SaveSchedule;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class EmailService {

    private final JavaMailSender mailSender;

    private static final String FROM_EMAIL = "noreply@kcc.com";
    private static final String SUBJECT_PREFIX = "KCC정보통신 | ";
    private static final String HTML_CONTENT_TEMPLATE =
            "<div class='email-container' style='max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);'>"
                    + "<div class='header' style='background-color: #0056b3; color: #ffffff; padding: 20px; text-align: center; border-top-left-radius: 8px; border-top-right-radius: 8px; font-size: 24px;'>"
                    + "    일정 초대"
                    + "</div>"
                    + "<div class='content' style='padding: 20px; line-height: 1.6;'>"
                    + "    <h2 style='color: #333333;'>안녕하세요,</h2>"
                    + "    <p>귀하는 다음 일정에 초대되었습니다:</p>"
                    + "    <p><strong>일정 제목:</strong> {{TITLE}}</p>"
                    + "    <p><strong>일정 기간:</strong> {{START_DATE}} ~ {{END_DATE}}</p>"
                    + "    <a href='trioffice.site/notifications' class='button' style='display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #0056b3; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold;'>일정 확인하기</a>"
                    + "</div>"
                    + "<div class='footer' style='text-align: center; padding: 10px; font-size: 12px; color: #999999; border-top: 1px solid #dddddd; margin-top: 20px;'>"
                    + "    <p>본 알림은 시스템에 의해 자동 생성되었습니다.</p>"
                    + "</div>"
                    + "</div>";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendScheduleInvitation(String recipientEmail, SaveSchedule saveSchedule) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(FROM_EMAIL);
        helper.setTo(recipientEmail);
        helper.setSubject(SUBJECT_PREFIX + saveSchedule.getName() + " 일정 초대 알림");

        String htmlContent = HTML_CONTENT_TEMPLATE
                .replace("{{TITLE}}", saveSchedule.getName())
                .replace("{{START_DATE}}", saveSchedule.getStartedDt().toString())
                .replace("{{END_DATE}}", saveSchedule.getEndedDt().toString());

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
