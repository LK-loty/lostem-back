package loty.lostem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.MailAuthDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    // createEmail로 SimpleMailMessage 객체를 생성받아 주입받은 mailSender 의 sender 메서드에 담아 메일 발송
    public void sendEmail(MailAuthDTO mailAuthDTO) {
        MimeMessage mailMessage = createEmail(mailAuthDTO.getEmail(), mailAuthDTO.getAuthCode());
        log.info("메일 생성 완료");
        try {
            mailSender.send(mailMessage);
            log.info("메일 발송");
        } catch (RuntimeException e) {
            log.info("메일 발송 실패");
        }
    }

    // 발송할 이메일 데이터 설정
    @Transactional
    public MimeMessage createEmail(String toEmail, String authCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            //mimeMessage.setFrom();
            /*mimeMessage.setRecipients(MimeMessage.RecipientType.TO, toEmail);
            mimeMessage.setSubject("Lostem 인증 번호");

            String body = "";
            body += "<h3>" + "인증번호를 발송해드립니다." + "</h3>";
            body += "<h1>" + authCode + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            mimeMessage.setText(body,"UTF-8", "html");*/

            helper.setTo(toEmail);
            helper.setSubject("Lostem 인증 번호");

            String body = "<h3>인증번호를 발송해드립니다.</h3>" +
                    "<h1>" + authCode + "</h1>" +
                    "<h3>감사합니다.</h3>";
            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return mimeMessage;
    }
}
