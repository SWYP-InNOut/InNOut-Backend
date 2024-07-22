package inandout.backend.Util;

import inandout.backend.common.exception.MemberException;
import inandout.backend.entity.member.Member;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.FAILED_SEND_EMAIL;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailUtils {
    @Value("${mail.username}")
    private String email;
    @Value("${spring.mail.request_Uri}")
    private String requestUri;

    private final JavaMailSender mailSender;

    public void sendEmail(Member member) {
        String receiverMail = member.getEmail();
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.addRecipients(MimeMessage.RecipientType.TO, receiverMail);// 보내는 대상
            message.setSubject("in&out 회원가입 이메일 인증");// 제목

            log.info(member.getAuthToken());
            log.info(email);

            String body = "<div>"
                    + "<h1> 안녕하세요. in&out 입니다</h1>"
                    + "<br>"
                    + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
                    + "<a href='" + requestUri + member.getAuthToken() + "'>인증 링크</a>"
                    + "</div>";

            message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
            // 보내는 사람의 이메일 주소, 보내는 사람 이름
            message.setFrom(new InternetAddress(email, "inandout"));// 보내는 사람
            mailSender.send(message); // 메일 전송
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error(FAILED_SEND_EMAIL.getMessage());
            throw new MemberException(FAILED_SEND_EMAIL);
        }
    }
}
