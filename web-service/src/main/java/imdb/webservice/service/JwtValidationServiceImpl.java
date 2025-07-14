package imdb.webservice.service;

import imdb.webservice.mapper.JwtMapper;
import jakarta.servlet.http.HttpSession;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtValidationServiceImpl implements JwtValidationService {
    @Value("${app.security.secret-key}")
    private String secret;
    private final JwtMapper mapper;

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

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String getTokenFromSession(HttpSession session) {
        String raw = (String) session.getAttribute("jwt");
        return mapper.extractToken(raw);
    }
}
