package imdb.verificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService{
    private final JavaMailSender javaMailSender;
    private final CodeService codeService;

    @Override
    public Long generateAndSendCode(String email) throws MessagingException {
        Long code = codeService.generateCode(email);
        sendEmail(email, code);
        return code;
    }

    @Override
    public void sendEmail(String email,Long code) throws MessagingException {
        MimeMessage mimeMessage = createMimeMessage();
        MimeMessageHelper mimeMessageHelper = createMimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(codeService.generateCode(email).toString());
        mimeMessageHelper.setText("Код подтверждения");
        javaMailSender.send(mimeMessage);
    }

    @Lookup
    protected MimeMessage createMimeMessage() {
        return null;
    }

    @Lookup
    protected MimeMessageHelper createMimeMessageHelper(MimeMessage mimeMessage) {
        return null;
    }
}
