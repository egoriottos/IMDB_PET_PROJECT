package imdb.userservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> registrationException(RegistrationException e) {
        log.error("Message: {}, status: {}", e.getMessage(), e.getHttpStatus());
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
}
