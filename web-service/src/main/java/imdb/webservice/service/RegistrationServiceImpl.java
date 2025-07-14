package imdb.webservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService{
    @Value("${app.urls.register}")
    private String registrationUrl;
    @Value("${app.urls.registerAdmin}")
    private String registrationAdminUrl;
    private final RestService restService;

    @Override
    public String registration(String username, String password, String email, Long age, String number) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String,String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);
        requestBody.put("email", email);
        requestBody.put("age", age.toString());
        requestBody.put("number", number);
        return restService.post(registrationUrl,requestBody,httpHeaders,String.class);
    }

    @Override
    public String registrationAdmin(String username, String password, String email, Long age, String number) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);
        requestBody.put("email", email);
        requestBody.put("age", age.toString());
        requestBody.put("number", number);
        return restService.post(registrationAdminUrl,requestBody,httpHeaders,String.class);

    }
}
