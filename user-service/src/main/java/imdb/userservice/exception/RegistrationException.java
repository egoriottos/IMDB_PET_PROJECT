package imdb.userservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RegistrationException extends RuntimeException {

    private static final String MESSAGE = "Registration error";
    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public RegistrationException() {
        super(MESSAGE);
    }

}
