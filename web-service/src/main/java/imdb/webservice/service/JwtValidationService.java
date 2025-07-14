package imdb.webservice.service;

import jakarta.servlet.http.HttpSession;

public interface JwtValidationService {
    boolean validateToken(String token);

    String getTokenFromSession(HttpSession session);

}
