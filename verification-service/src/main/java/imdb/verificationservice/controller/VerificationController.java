package imdb.verificationservice.controller;

import imdb.verificationservice.service.CodeService;
import imdb.verificationservice.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("verification-service")
@RequiredArgsConstructor
@Slf4j
public class VerificationController {
    private final MailService mailService;
    private final CodeService codeService;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody String email) {
        try {
            Long code = mailService.generateAndSendCode(email);
            log.info("code {} was sent in email {}", code,email);
            return ResponseEntity.ok("Verification code sent to " + email);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification code");
        }
    }
    @GetMapping("/get-code")
    public ResponseEntity<Boolean> getCode(@RequestParam String email, @RequestParam String code) {
        return ResponseEntity.ok(codeService.validateCode(email,code));
    }
}
