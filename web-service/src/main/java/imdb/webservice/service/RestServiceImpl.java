package imdb.webservice.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestServiceImpl implements RestService {
    private final RestClient restClient;

    @Override
    public <T, R> R post(String url, T body, HttpHeaders headers, Class<R> responseType) {
        return exchange(url,HttpMethod.POST, body, headers, responseType);
    }

    @Override
    public <R> R get(String url, HttpHeaders headers, Class<R> responseType) {
        return exchange(url, HttpMethod.GET, null, headers, responseType);
    }

    @Override
    public <T, R> R delete(String url, HttpHeaders headers, Class<R> responseType) {
        return exchange(url,HttpMethod.DELETE, null, headers, responseType);
    }

    @Override
    public <R> R get(String url, HttpHeaders headers, ParameterizedTypeReference<R> responseType) {
        return restClient.get()
                .uri(url)
                .headers(h -> h.addAll(headers))
                .retrieve()
                .body(responseType);
    }

    private <T, R> R exchange(String url, HttpMethod httpMethod, @Nullable T body, HttpHeaders headers, Class<R> responseType) {
        try {
            RestClient.RequestBodySpec request;
            request = restClient.method(httpMethod)
                    .uri(url)
                    .headers(h -> h.addAll(headers));

            if (body != null) {
                request.body(body);
            }

            return request
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), (req, res) -> {
                        throw new RuntimeException("HTTP error " + res.getStatusCode());
                    })
                    .body(responseType);
        } catch (RestClientException e) {
            throw new RuntimeException("Request failed", e);
        }
    }
}
