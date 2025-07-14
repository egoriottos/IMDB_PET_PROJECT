package imdb.searchservice.service;

import org.springframework.http.HttpHeaders;

public interface JwtValidationService {
    boolean validateToken(String token);
    HttpHeaders createHeaders(String token);
}
