package imdb.webservice.service;

import imdb.webservice.dto.AuthenticationResponse;
import imdb.webservice.dto.ConfirmAuthRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService{
    @Value("${app.urls.codeFromCache}")
    private String verifyUrl;
    @Value("${app.urls.token}")
    private String tokenUrl;
    private final RestClient restClient;
    @Override
    public AuthenticationResponse verify(String code, HttpSession session) {
        String email = (String) session.getAttribute("verificationEmail");
        if(email == null) {
            throw new IllegalArgumentException("Email не найден в сессии, возможно сессия истекла");
        }
        boolean isCodeValid = Boolean.TRUE.equals(restClient.get()
                .uri(verifyUrl + "?email={email}&code={code}", email, code)
                .retrieve()
                .body(Boolean.class));
        System.out.println("isCodeValid:"+ isCodeValid);
        if(!isCodeValid) {
            throw new VerifyError("Неверный код подтверждения");
        }
        ConfirmAuthRequest request = new ConfirmAuthRequest((String) session.getAttribute("username"),code);
        System.out.println("request:" + request.getUsername() + ", " + request.getVerificationCode());
        return restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(AuthenticationResponse.class);
    }
}
