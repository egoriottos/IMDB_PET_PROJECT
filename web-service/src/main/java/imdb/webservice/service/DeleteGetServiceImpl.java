package imdb.webservice.service;

import imdb.webservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteGetServiceImpl implements DeleteGetService {
    @Value("${app.urls.delete}")
    private String deleteUrl;
    @Value("${app.urls.getAll}")
    private String getAllUrl;
    private final RestClient restClient;

    @Override
    public String deleteUser(Long id, String jwt) {
        String url = deleteUrl.replace("{id}", id.toString());
        log.info("Attempting to DELETE {} with token: {}", url, jwt);
        try {
            return restClient.delete()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            log.error("DELETE request failed. Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    @Override
    public List<UserDto> getAll(String jwt) {
        log.info("Attempting to call {} with token: {}", getAllUrl, jwt);

        try {
            UserDto[] array = restClient.get()
                    .uri(getAllUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .retrieve()
                    .body(UserDto[].class);

            return Arrays.asList(array);
        } catch (HttpClientErrorException e) {
            log.error("Request failed. Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }
}
