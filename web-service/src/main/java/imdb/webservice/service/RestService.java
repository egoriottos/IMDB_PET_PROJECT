package imdb.webservice.service;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

public interface RestService {
    <T, R> R post(String url, T body, HttpHeaders headers, Class<R> responseType);
    <R> R get(String url, HttpHeaders headers, Class<R> responseType);
    <T, R> R delete(String url, HttpHeaders headers, Class<R> responseType);
    <R> R get(String url, HttpHeaders headers, ParameterizedTypeReference<R> responseType);
}
