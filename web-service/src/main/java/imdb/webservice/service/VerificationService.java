package imdb.webservice.service;

import imdb.webservice.dto.AuthenticationResponse;
import jakarta.servlet.http.HttpSession;

public interface VerificationService {
    AuthenticationResponse verify(String code, HttpSession session);
}
