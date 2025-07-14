package imdb.searchservice.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtValidationServiceImpl implements JwtValidationService{
    @Value("${app.security.secret-key}")
    private String secret;
    @Override
    public boolean validateToken(String token) {
        SecretKey key = getSignInKey();
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token was expired: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Error validation token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
