package imdb.verificationservice.service;

import jakarta.mail.MessagingException;

/**
 * Сервис для отправки кода на почту
 */
public interface MailService {
    /**
     * Отправляет код подтверждения на почту
     *
     * @param code код подтверждения
     * @param email почта
     */
    void sendEmail(String email,Long code) throws MessagingException;
    Long generateAndSendCode(String email) throws MessagingException;
}
