package inandout.backend.service.login;

import inandout.backend.common.exception.MemberException;
import inandout.backend.config.SecurityConfig;
import inandout.backend.dto.login.JoinDTO;
import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.Member;
import inandout.backend.entity.member.MemberStatus;
import inandout.backend.repository.login.MemberRepository;
import inandout.backend.validator.MemberValidator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender mailSender;
    @Value("${mail.username}")
    private String email;

    public void joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();

        MemberValidator.validateDuplicateEmail(memberRepository, email);
        MemberValidator.validateDuplicateUsername(memberRepository, username);

        String authToken = UUID.randomUUID().toString();

        Member member = Member.createGeneralMember(username, email, bCryptPasswordEncoder.encode(password), Platform.GENERAL);
        member.updateToken(authToken);

        memberRepository.save(member);
        sendEmail(member);
    }

    public void sendEmail(Member member) {
        String receiverMail = member.getEmail();
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.addRecipients(MimeMessage.RecipientType.TO, receiverMail);// 보내는 대상
            message.setSubject("Artify 회원가입 이메일 인증");// 제목

            log.info(member.getAuthToken());

            String body = "<div>"
                    + "<h1> 안녕하세요. in&out 입니다</h1>"
                    + "<br>"
                    + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
                    + "<a href='http://localhost:9000/auth/verify?token=" + member.getAuthToken() + "'>인증 링크</a>"
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

    public Optional<Member> updateByVerifyToken(String token) {
        Optional<Member> member = memberRepository.findByAuthToken(token);

        if (member.isPresent()) {
            member.get().updateStatus(MemberStatus.ACTIVE);  // 변경감지
            return member;
        } else {
            return Optional.empty();
        }
    }
}
