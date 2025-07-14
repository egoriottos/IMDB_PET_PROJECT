package imdb.verificationservice.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Configuration
public class MailConfig {

    @Bean
    @Scope("prototype")
    public MimeMessage mimeMessage(JavaMailSender javaMailSender) {
        return javaMailSender.createMimeMessage();
    }

    @Bean
    @Scope("prototype")
    public MimeMessageHelper mimeMessageHelper(MimeMessage mimeMessage) throws MessagingException {
        return new MimeMessageHelper(mimeMessage,true,"UTF-8");
    }
}
